# uni-resolver-driver-did-weid
A Universal Resolver driver for did:weid identifiers.

## Specifications

- [Decentralized Identifiers](https://w3c.github.io/did-core/)
- [DID Method Specification](https://weidentity.readthedocs.io/zh_CN/latest/docs/weidentity-spec.html)

## Example DIDs

```text
did:weid:101:0x0086eb1f712ebc6f1c276e12ec21
did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8
```

## Project Structure

```text
.
├── LICENSE
├── README.md
├── build.gradle
├── gradle
├── gradlew
├── gradlew.bat
├── settings.gradle
└── src
    ├── main
    │   ├── java
    │   │   └── uniresolver
    │   │       └── driver
    │   │           └── did
    │   │               └── weid
    │   │                   └── DIDWeidDriver.java
    │   └── resources
    │       ├── conf
    │       │   ├── amop
    │       │   │   ├── consumer_private_key.p12
    │       │   │   └── consumer_public_key.pem
    │       │   └── gm
    │       │       ├── sm_ca.crt
    │       │       ├── sm_ensdk.crt
    │       │       ├── sm_ensdk.key
    │       │       ├── sm_sdk.crt
    │       │       └── sm_sdk.key
    │       ├── fisco.properties
    │       └── weidentity.properties
    └── test
        ├── java
        │   └── uniresolver
        │       └── examples
        │           └── TestLocalUniResolver.java
        └── resources

```

## Build and Run (Docker)

```bash
docker build -f ./docker/Dockerfile . -t universalresolver/driver-did-weid
docker run -p 8080:8080 universalresolver/driver-did-weid

# use weid in you scope to test
curl -X GET http://localhost:8080/1.0/identifiers/did:weid:iot:0x772b4982bebc911b66cf0793de3efe463e442af8
```

## Driver Variables

Following the
document [https://weidentity.readthedocs.io/zh_CN/release-3.1.1/](https://weidentity.readthedocs.io/zh_CN/release-3.1.1/)
to modify `resources/conf/fisco.properties` and `weidentity.properties` to connect to your specific fiscobcos blockchain
network and Weidentity service.

