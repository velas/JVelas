package velas.crypto;

import net.i2p.crypto.eddsa.EdDSAPrivateKey;
import net.i2p.crypto.eddsa.EdDSAPublicKey;
import net.i2p.crypto.eddsa.spec.*;

import java.security.*;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Hashtable;

final class KeyPairGenerator extends KeyPairGeneratorSpi {
    private static final int DEFAULT_KEYSIZE = 256;
    private EdDSAParameterSpec edParams;
    private SecureRandom random;
    private boolean initialized;
    private static final Hashtable<Integer, AlgorithmParameterSpec> edParameters = new Hashtable();

    public KeyPairGenerator() {
    }

    public void initialize(int keysize, SecureRandom random) {
        AlgorithmParameterSpec edParams = (AlgorithmParameterSpec)edParameters.get(keysize);
        if (edParams == null) {
            throw new InvalidParameterException("unknown key type.");
        } else {
            try {
                this.initialize(edParams, random);
            } catch (InvalidAlgorithmParameterException var5) {
                throw new InvalidParameterException("key type not configurable.");
            }
        }
    }

    public void initialize(AlgorithmParameterSpec params, SecureRandom random) throws InvalidAlgorithmParameterException {
        if (params instanceof EdDSAParameterSpec) {
            this.edParams = (EdDSAParameterSpec)params;
        } else {
            if (!(params instanceof EdDSAGenParameterSpec)) {
                throw new InvalidAlgorithmParameterException("parameter object not a EdDSAParameterSpec");
            }

            this.edParams = this.createNamedCurveSpec(((EdDSAGenParameterSpec)params).getName());
        }

        this.random = random;
        this.initialized = true;
    }

    public KeyPair generateKeyPair() {
        if (!this.initialized) {
            this.initialize(256, new SecureRandom());
        }

        byte[] seed = new byte[this.edParams.getCurve().getField().getb() / 8];
        this.random.nextBytes(seed);
        EdDSAPrivateKeySpec privKey = new EdDSAPrivateKeySpec(seed, this.edParams);
        EdDSAPublicKeySpec pubKey = new EdDSAPublicKeySpec(privKey.getA(), this.edParams);
        return new KeyPair(new EdDSAPublicKey(pubKey), new EdDSAPrivateKey(privKey));
    }

    protected EdDSANamedCurveSpec createNamedCurveSpec(String curveName) throws InvalidAlgorithmParameterException {
        EdDSANamedCurveSpec spec = EdDSANamedCurveTable.getByName(curveName);
        if (spec == null) {
            throw new InvalidAlgorithmParameterException("unknown curve name: " + curveName);
        } else {
            return spec;
        }
    }

    static {
        edParameters.put(256, new EdDSAGenParameterSpec("Ed25519"));
    }
}
