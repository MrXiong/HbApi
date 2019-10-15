package com.i.hbapi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.HDUtils;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.crypto.MnemonicException;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;

import java.security.SecureRandom;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WalletActivity extends AppCompatActivity {

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
            //这个jar包里面内置的有一份english.txt
            //bitcoinj-core-0.15.5.jar\org\bitcoinj\crypto\mnemonic\wordlist\english.txt
            List<String> words = createMnemonics();
            Log.v("tag", "助记词：" + words.toString());
            //通过助记词生成种子
            byte[] seeds = MnemonicCode.INSTANCE.toEntropy(words);
            DeterministicSeed deterministicSeed = new DeterministicSeed(words.toString(), seeds, "password", Utils.currentTimeSeconds());

            NetworkParameters networkParameters = TestNet3Params.get();
            Wallet wallet = Wallet.fromSeed(networkParameters, deterministicSeed, Script.ScriptType.P2PK);
            Address address = wallet.freshReceiveAddress();
            String addressstr = address.getHash().toString();
            Coin balance = wallet.getBalance();
            long value = balance.getValue();
            //BIP32 标准定义了 HD 钱包的生成规则。HD 钱包中的所有层级密钥都是由根种子推导而来，通常根种子由上述步骤 BIP39 生成。所以只需通过助记词就能备份和恢复钱包，这也是 HD 钱包的缺陷，如果你的根种子泄漏，那么全部密钥随之都泄漏。
            DeterministicKey masterPrivateKey = HDKeyDerivation.createMasterPrivateKey(seeds);
            //byte[] chainCode = masterPrivateKey.getChainCode();
            byte[] pubKey = masterPrivateKey.getPubKey();
            String publicKeyAsHex = masterPrivateKey.getPublicKeyAsHex();
            DeterministicKey deterministicKey = HDKeyDerivation.deriveChildKey(masterPrivateKey, 0);

        } catch (UnreadableWalletException e) {
            e.printStackTrace();
        } catch (MnemonicException.MnemonicLengthException e) {
            e.printStackTrace();
        } catch (MnemonicException.MnemonicWordException e) {
            e.printStackTrace();
        } catch (MnemonicException.MnemonicChecksumException e) {
            e.printStackTrace();
        }
    }
}
