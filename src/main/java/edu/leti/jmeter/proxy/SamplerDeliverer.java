package edu.leti.jmeter.proxy;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;

/**
 * Интерфейс, отвечающий за обработку http запросов.
 *
 * @author Tedikova O.
 * @version 1.0
 */
public interface SamplerDeliverer {
    /**
     * Метод обрабатывет полученный http запрос.
     *
     * @param sampler    http sampler, созданный на основе полученного запроса.
     * @param subConfigs настройки тестового элемента.
     * @param result     информация об обработке запроса.
     */
    public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result);
}
