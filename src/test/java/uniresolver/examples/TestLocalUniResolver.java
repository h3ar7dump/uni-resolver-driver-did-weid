package uniresolver.examples;

import org.junit.jupiter.api.Test;
import uniresolver.ResolutionException;
import uniresolver.driver.did.weid.DIDWeidDriver;
import uniresolver.local.LocalUniResolver;
import uniresolver.result.ResolveDataModelResult;

import java.util.HashMap;
import java.util.Map;

public class TestLocalUniResolver {
    @Test
    public void test() throws ResolutionException {

        // change here to your own DID
        String did = "did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8";
        LocalUniResolver uniResolver = new LocalUniResolver();
        uniResolver.getDrivers().add(new DIDWeidDriver());

        Map<String, Object> resolveOptions = new HashMap<>();
        resolveOptions.put("accept", "application/did+ld+json");

        ResolveDataModelResult resolveResult;
        resolveResult = uniResolver.resolve(did, resolveOptions);
        System.out.println(resolveResult.toJson());
    }

}
