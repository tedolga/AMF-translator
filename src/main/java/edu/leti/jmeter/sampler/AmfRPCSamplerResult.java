package edu.leti.jmeter.sampler;

import org.apache.jmeter.samplers.SampleResult;

import java.util.Arrays;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public class AmfRPCSamplerResult extends SampleResult {

    private AmfRPCSampler amfRPCSampler;

    public AmfRPCSamplerResult(AmfRPCSampler amfRPCSampler) {
        this.amfRPCSampler = amfRPCSampler;
    }

    @Override
    public String getSamplerData() {

        StringBuilder samplerData = new StringBuilder();
        samplerData.append("Endpoint Url: ");
        samplerData.append(amfRPCSampler.getEndpointUrl());
        samplerData.append("\n");
        samplerData.append("Amf Call: ");
        samplerData.append(amfRPCSampler.getAmfCall());
        samplerData.append("\n");
        samplerData.append("Request parameters: ");
        samplerData.append(Arrays.toString(amfRPCSampler.getParameters().toArray()));

        return samplerData.toString();
    }

    @Override
    public String getDataType() {
        return SampleResult.TEXT;
    }
}
