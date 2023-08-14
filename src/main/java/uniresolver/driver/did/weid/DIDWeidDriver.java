package uniresolver.driver.did.weid;

import com.webank.weid.blockchain.protocol.response.ResponseData;
import com.webank.weid.protocol.base.AuthenticationProperty;
import com.webank.weid.protocol.base.ServiceProperty;
import com.webank.weid.protocol.base.WeIdDocument;
import com.webank.weid.protocol.base.WeIdDocumentMetadata;
import com.webank.weid.service.impl.WeIdServiceImpl;
import com.webank.weid.service.rpc.WeIdService;
import foundation.identity.did.DID;
import foundation.identity.did.DIDDocument;
import foundation.identity.did.Service;
import foundation.identity.did.VerificationMethod;
import foundation.identity.jsonld.JsonLDUtils;
import uniresolver.ResolutionException;
import uniresolver.driver.AbstractDriver;
import uniresolver.driver.Driver;
import uniresolver.result.ResolveDataModelResult;

import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DIDWeidDriver extends AbstractDriver implements Driver {
    public static final String METHOD_NAME = "weid";
    public static final List<URI> DIDDOCUMENT_CONTEXTS = List.of(
            URI.create("https://github.com/WeBankFinTech/WeIdentity/blob/master/context/v1")
    );
    public static final String[] DIDDOCUMENT_VERIFICATIONMETHOD_KEY_TYPES = new String[]{"SM2VerificationKey", "Ed25519VerificationKey2018"};
    private final WeIdService weIdService = new WeIdServiceImpl();

    public DIDWeidDriver() {
        super();
    }

    @Override
    public ResolveDataModelResult resolve(DID did, Map<String, Object> resolveOptions) throws ResolutionException {
        ResponseData<WeIdDocumentMetadata> documentMetadataResult = weIdService.getWeIdDocumentMetadata(did.toString());
        ResponseData<WeIdDocument> weIdDocumentResult = weIdService.getWeIdDocument(did.toString());
        // assemble DID Resolution Metadata
        Map<String, Object> didResolutionMetadata = new LinkedHashMap<>();
        if (weIdDocumentResult.getResult() == null || documentMetadataResult.getResult() == null) {
            didResolutionMetadata.put("error", documentMetadataResult.getErrorMessage());
            didResolutionMetadata.put("errorCode", documentMetadataResult.getErrorCode());
            return ResolveDataModelResult.build(didResolutionMetadata, null, null);
        }
        didResolutionMetadata.put("contentType", "application/did+ld+json");
        didResolutionMetadata.put("error", null);

        didResolutionMetadata.put("transactionInfo", weIdDocumentResult.getTransactionInfo());

        boolean deactivated = documentMetadataResult.getResult().isDeactivated();

        didResolutionMetadata.put("contentType", "application/did+ld+json");
        didResolutionMetadata.put("error", null);

        DIDDocument didDocument;
        Map<String, Object> didDocumentMetadata = new LinkedHashMap<>();
        if (!deactivated) {
            // assemble DID Document Metadata
            didDocumentMetadata.put("created", documentMetadataResult.getResult().getCreated());
            didDocumentMetadata.put("updated", documentMetadataResult.getResult().getUpdated());
            didDocumentMetadata.put("deactivated", deactivated);
            didDocumentMetadata.put("versionId", documentMetadataResult.getResult().getVersionId());

            // assemble DID document
            // assemble authentication
            List<AuthenticationProperty> authenticationPropertys = weIdDocumentResult.getResult().getAuthentication();
            List<VerificationMethod> verificationMethods = new ArrayList<>();
            for (AuthenticationProperty authenticationProperty : authenticationPropertys
            ) {
                VerificationMethod verificationMethod = VerificationMethod.builder()
                        .id(URI.create(authenticationProperty.getId()))
                        .type(authenticationProperty.getType())
                        .controller(URI.create(authenticationProperty.getController()))
                        .publicKeyMultibase(authenticationProperty.getPublicKeyMultibase())
                        .build();
                JsonLDUtils.jsonLdAdd(verificationMethod, "publicKey", authenticationProperty.getPublicKey());
                verificationMethods.add(verificationMethod);
            }

            // assemble service
            List<ServiceProperty> serviceProperties = weIdDocumentResult.getResult().getService();
            List<Service> services = new ArrayList<>();
            for (ServiceProperty serviceProperty : serviceProperties
            ) {
                Service service = Service.builder()
                        .id(URI.create(serviceProperty.getId()))
                        .type(serviceProperty.getType())
                        .serviceEndpoint(serviceProperty.getServiceEndpoint())
                        .build();
                services.add(service);
            }

            didDocument = DIDDocument.builder()
                    .defaultContexts(false)
                    .forceContextsArray(false)
                    .context(URI.create(DIDDOCUMENT_CONTEXTS.get(0).toString()))
                    .id(URI.create(did.toString()))
                    .authenticationVerificationMethods(verificationMethods)
                    .services(services)
                    .build();
        } else {
            didDocument = null;
        }
        // create DID DOCUMENT METADATA
        // create RESOLVE RESULT

        ResolveDataModelResult resolveDataModelResult = ResolveDataModelResult.build(didResolutionMetadata, didDocument, didDocumentMetadata);
        return resolveDataModelResult;
    }

}
