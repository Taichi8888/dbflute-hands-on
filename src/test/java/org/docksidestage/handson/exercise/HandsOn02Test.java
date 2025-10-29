package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// TODO jflute 次回1on1, section2からのふぉろー (2025/10/14)

// #1on1: section1の振り返り
// o 置くだけのMySQLの話
// o Maven/Gradle: Mavenはxmlでレール敷いてる(安定)、Gradleはscriptで自由度が高い
// o 特徴の違いで覚えておくといい
// o Gradle (でビルドするとき) も、Mavenサーバーを使っている e.g. mavenCentral()
// TODO done hase [読み物課題] 応援してる "A" にもデメリットはあるよ by jflute (2025/10/14)
// https://jflute.hatenadiary.jp/entry/20181008/yourademerit

// TODO jflute いつか1on1で、IntelliJの.ideaのお話 (2025/10/14)
// https://dbflute.seasar.org/ja/tutorial/handson/section01.html#ideashared

// TODO done hase DBFluteクライアントのlogのログファイルをgitignoreにお願いします by jflute (2025/10/14)
// 引用:
// ハンズオンをどこかの Git で管理する場合は、logファイルを .gitignore に登録しておきましょう。
// (logディレクトリに.gitignoreファイルを作成し、*.logと指定しておくでOKです)

// TODO done hase javadocお願い by jflute (2025/10/14)

/**
 * セクション2のテストクラス
 * <ul>
 *     <li>テストデータ登録の確認</li>
 *     <li>会員名称がSで始まる会員を検索</li>
 *     <li>会員IDが1の会員を検索</li>
 *     <li>生年月日がない会員を検索</li>
 * </ul>
 *
 * @author hase
 */
public class HandsOn02Test extends UnitContainerTestCase{

    @Resource
    private MemberBhv memberBhv;
    
    // #1on1: Arrange, Act, Assertの話、Actはテスト対象の処理のみ
    // #1on1: 補完テンプレートの _ll, _li をぜひ
    public void test_existsTestData() throws Exception {
        // ## Arrange ##
        
        // ## Act ##
        // TODO done hase 戻り値はlongじゃなくて int  by jflute (2025/10/14)
        // (IntelliJの補完で、戻り値を導出するようにしましょう: .var )
        int count = memberBhv.selectCount(cb -> {});
    
        // ## Assert ##
        assertTrue(count > 0);
    }
    public void test_candidateNameStartsWithS() throws Exception {
        // ## Arrange ##
        
        
        // ## Act ##

        // ## Assert ##
    }
}
