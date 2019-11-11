package com.i.hbapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.i.hbapi.utils.Bip44Utils;
import com.i.hbapi.utils.ByteUtils;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicHierarchy;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.spongycastle.crypto.digests.SHA512Digest;
import org.spongycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.spongycastle.crypto.params.KeyParameter;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.WalletFile;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static org.spongycastle.asn1.ua.DSTU4145NamedCurves.params;

public class WalletActivity extends AppCompatActivity {

    private static final String TAG = "WalletActivity";
    private static final int BTC_COIN = 1;
    private static final int ETH_COIN = 2;
    private static final boolean BTC_TEST_NET = true;
    @BindView(R.id.btn_seed)
    Button btnSeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        ButterKnife.bind(this);


    }


    public List<String> createMnemonics() {
        try {
            SecureRandom secureRandom = new SecureRandom();
            byte[] entropy = new byte[DeterministicSeed.DEFAULT_SEED_ENTROPY_BITS / 8];
            secureRandom.nextBytes(entropy);

            return MnemonicCode.INSTANCE.toMnemonic(entropy);
        } catch (MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        }
        return null;
    }

    @OnClick(R.id.btn_seed)
    public void onViewClicked() {


        try {
            String huobiSeed = "omit fiction assist rent buyer crunch you inmate access room vague during";
            //这个jar包里面内置的有一份english.txt
            //bitcoinj-core-0.15.5.jar\org\bitcoinj\crypto\mnemonic\wordlist\english.txt
            //List<String> words = createMnemonics();


            byte[] seeds = generateSeed(huobiSeed, null);

            //BIP32 标准定义了 HD 钱包的生成规则。HD 钱包中的所有层级密钥都是由根种子推导而来，通常根种子由上述步骤 BIP39 生成。所以只需通过助记词就能备份和恢复钱包，这也是 HD 钱包的缺陷，如果你的根种子泄漏，那么全部密钥随之都泄漏。

            //ETH地址创建
            DeterministicKey rootPrivateKey = HDKeyDerivation.createMasterPrivateKey(seeds);
            rootPrivateKey.getChainCode();
            // 4. 由根私钥生成 第一个HD 钱包
            DeterministicHierarchy dh = new DeterministicHierarchy(rootPrivateKey);

            // 5. 定义父路径 H则是加强 imtoken中的eth钱包进过测试发现使用的是此方式生成 bip44





            String BTCPath = "M/44H/0H/0H/0";
            String ETHPath = "M/44H/60H/0H/0";
            List<ChildNumber> parentPath = HDUtils.parsePath(ETHPath);
            // 6. 由父路径,派生出第一个子私钥 "new ChildNumber(0)" 表示第一个 （m/44'/60'/0'/0/0）
            DeterministicKey child = dh.deriveChild(parentPath, true, true, new ChildNumber(0));
            Log.i(TAG, "私钥ashex: " + child.getPrivateKeyAsHex());


            //通过私钥生成公钥和地址
            byte[] privateKeyByte = child.getPrivKeyBytes();
            ECKeyPair keyPair = ECKeyPair.create(privateKeyByte);
            Log.i(TAG, "generateBip44Wallet: 钥匙对  私钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPrivateKey()));
            Log.i(TAG, "generateBip44Wallet: 钥匙对  公钥 = " + Numeric.toHexStringNoPrefix(keyPair.getPublicKey()));

            WalletFile walletFile = org.web3j.crypto.Wallet.createLight("123456", keyPair);
            String address = Keys.toChecksumAddress(walletFile.getAddress());
            Log.i(TAG, "address:" + address);
            Gson gson = new Gson();
            String keystore = gson.toJson(walletFile);
            Log.i(TAG, "keystore:" + keystore);




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final int SEED_ITERATIONS = 2048;
    private static final int SEED_KEY_SIZE = 512;

    public static byte[] generateSeed(String mnemonic, String passphrase) {
        validateMnemonic(mnemonic);
        passphrase = passphrase == null ? "" : passphrase;


        String salt = String.format("mnemonic%s", passphrase);
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(mnemonic.getBytes(Charset.forName("UTF-8")), salt.getBytes(Charset.forName("UTF-8")), SEED_ITERATIONS);

        return ((KeyParameter) gen.generateDerivedParameters(SEED_KEY_SIZE)).getKey();
    }

    private static void validateMnemonic(String mnemonic) {
        if (mnemonic == null || mnemonic.trim().isEmpty()) {
            throw new IllegalArgumentException("Mnemonic is required to generate a seed");
        }
    }







    static class  ShellWallet{

    }




    public static List<ChildNumber> parsePath(@Nonnull String path) {
        String[] parsedNodes = path.replace("m", "").split("/");
        List<ChildNumber> nodes = new ArrayList<>();

        for (String n : parsedNodes) {
            n = n.replaceAll(" ", "");
            if (n.length() == 0) continue;
            boolean isHard = n.endsWith("'");
            if (isHard) n = n.substring(0, n.length() - 1);
            int nodeNumber = Integer.parseInt(n);
            nodes.add(new ChildNumber(nodeNumber, isHard));
        }

        return nodes;
    }



}
