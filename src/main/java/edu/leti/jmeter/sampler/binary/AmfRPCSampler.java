package edu.leti.jmeter.sampler.binary;

import flex.messaging.io.amf.client.AMFConnection;
import flex.messaging.io.amf.client.exceptions.ClientStatusException;
import flex.messaging.io.amf.client.exceptions.ServerStatusException;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.CollectionProperty;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.StringProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Реализация сэмплера, отправляющего AMF запросы.
 * Нагрузочное(многопоточное) тестирование в JMeter организовано так, что каждый поток прогоняет тест план,
 * имитируя работу отдельного пользователя. Данная реализация сэмплера обеспечивает создание отдельного соединения
 * с сервером для каждого пользователя (потока) и непрерывность сессии во время прохождения тест плана.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfRPCSampler extends AbstractSampler {

    private static final Logger logger = LoggerFactory.getLogger(AmfRPCSampler.class);

    private static final String ENDPOINT_URL = "endpoint_url";

    private static final String AMF_CALL = "amf_call";

    private static final String PARAMETERS = "request_parameters";

    private static final String PARAMS_TABLE = "parameters_table";

    /**
     * Переменная, содержащая свой экземпляр AMFConnection для каждого потока из ThreadGroup
     */
    private static final ThreadLocal<AMFConnection> AMF_CONNECTION_THREAD_LOCAL = new ThreadLocal<AMFConnection>();

    public String getEndpointUrl() {
        return getPropertyAsString(ENDPOINT_URL);
    }

    public void setEndpointUrl(String endpointUrl) {
        setProperty(ENDPOINT_URL, endpointUrl);
    }

    public void setAmfCall(String amfCall) {
        setProperty(AMF_CALL, amfCall);
    }

    public String getAmfCall() {
        return getPropertyAsString(AMF_CALL);
    }

    public void setParameters(List<String> parameters) {
        List<JMeterProperty> propertyList = new ArrayList<JMeterProperty>();
        for (String parameter : parameters) {
            propertyList.add(new StringProperty(parameter, parameter));
        }
        CollectionProperty property = new CollectionProperty(PARAMETERS, propertyList);
        setProperty(property);
    }

    public void setParamsTable(List<String[]> paramsTable) {
        List<JMeterProperty> propertyList = new ArrayList<JMeterProperty>();
        for (String[] row : paramsTable) {
            propertyList.add(new StringProperty(row[0], row[1]));
        }
        CollectionProperty property = new CollectionProperty(PARAMS_TABLE, propertyList);
        setProperty(property);
    }

    public List<String> getParameters() {
        List<String> result = new ArrayList<String>();
        JMeterProperty property = getProperty(PARAMETERS);
        if (property instanceof CollectionProperty) {
            CollectionProperty collectionProperty = (CollectionProperty) property;
            for (int i = 0; i < collectionProperty.size(); i++) {
                result.add(collectionProperty.get(i).getStringValue());
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public List<String[]> getParamsTable() {
        List<String[]> result = new ArrayList<String[]>();
        JMeterProperty property = getProperty(PARAMS_TABLE);
        if (property instanceof CollectionProperty) {
            CollectionProperty collectionProperty = (CollectionProperty) property;
            for (int i = 0; i < collectionProperty.size(); i++) {
                result.add(new String[]{collectionProperty.get(i).getName(), collectionProperty.get(i).getStringValue()});
            }
            return result;
        } else {
            return Collections.emptyList();
        }
    }

    public SampleResult sample(Entry entry) {
        AmfRPCSamplerResult sampleResult = new AmfRPCSamplerResult(this);
        sampleResult.setSampleLabel(getName());

        sampleResult.setSuccessful(true);
        /*
        * Проверка того, существует ли для данного потока AMFConnection. Если для данного потока AMFConnection не
        * существует, то создаётся новый экземпляр AMFConnection.
        */
        AMFConnection amfConnection = AMF_CONNECTION_THREAD_LOCAL.get();
        if (amfConnection == null) {
            try {
                amfConnection = new AMFConnection();
                amfConnection.connect(getEndpointUrl());
            } catch (ClientStatusException e) {
                logger.error("Couldn't connect to " + getEndpointUrl(), e);
            }
        }
        try {
            Object result = amfConnection.call(getAmfCall(), getParameters().toArray());
            sampleResult.setResponseData(result.toString(), "UTF-8");

        } catch (ClientStatusException cse) {
            handleException(cse, sampleResult);
        } catch (ServerStatusException sse) {
            handleException(sse, sampleResult);
        }
        return sampleResult;
    }

    private void handleException(Exception e, SampleResult sampleResult) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream stream = new PrintStream(outputStream);
        e.printStackTrace(stream);
        logger.error("Send request to " + getAmfCall() + "failed", e);
        sampleResult.setResponseData(new String(outputStream.toByteArray()), "UTF-8");
        sampleResult.setSuccessful(false);
    }

}
