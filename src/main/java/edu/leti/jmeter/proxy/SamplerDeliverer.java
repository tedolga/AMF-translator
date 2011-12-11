package edu.leti.jmeter.proxy;

import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;

/**
 * @author Tedikova O.
 * @version 1.0
 */
public interface SamplerDeliverer {

    public void deliverSampler(HTTPSamplerBase sampler, TestElement[] subConfigs, SampleResult result);
}
