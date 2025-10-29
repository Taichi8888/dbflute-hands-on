package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// done jflute 次回1on1, section2からのふぉろー (2025/10/14)
// #1on1: マスターデータとテストデータの違い (2025/10/29)
// (会員マスター話)
// #1on1: PK制約とは？ (PrimaryKey制約、主キー制約)
// #1on1: UQ制約とは？ (Unique制約)
// #1on1: FK制約とは？ (ForeignKey制約、外部キー制約)
// #1on1: NotNull制約とは？ (insert/update時にnullが登録できない)
// SchemaHTMLも一緒にみながら

// #1on1: section1の振り返り
// o 置くだけのMySQLの話
// o Maven/Gradle: Mavenはxmlでレール敷いてる(安定)、Gradleはscriptで自由度が高い
// o 特徴の違いで覚えておくといい
// o Gradle (でビルドするとき) も、Mavenサーバーを使っている e.g. mavenCentral()
// done hase [読み物課題] 応援してる "A" にもデメリットはあるよ by jflute (2025/10/14)
// https://jflute.hatenadiary.jp/entry/20181008/yourademerit
// #1on1: なぜコードの体裁にこだわるのか？フラッシュ記憶しやすいように。フラッシュ記憶できると...↓
// TODO hase [読み物課題] 別にパソコンがなくてもプログラミングはできるよ by jflute (2025/10/29)
// https://jflute.hatenadiary.jp/entry/20170923/nopcpg

// TODO jflute いつか1on1で、IntelliJの.ideaのお話 (2025/10/14)
// https://dbflute.seasar.org/ja/tutorial/handson/section01.html#ideashared

// done hase DBFluteクライアントのlogのログファイルをgitignoreにお願いします by jflute (2025/10/14)
// 引用:
// ハンズオンをどこかの Git で管理する場合は、logファイルを .gitignore に登録しておきましょう。
// (logディレクトリに.gitignoreファイルを作成し、*.logと指定しておくでOKです)
// TODO hase 既存のコミット済みのものはそのままなので削除コミットしましょう by jflute (2025/10/29)
// (.gitignoreのニュアンス、今後コミットするときにignoreにするって感じ)
// #1on1: .gitignore戦略の話 (設定ファイルを綺麗に、階層をうまく使う)

// done hase javadocお願い by jflute (2025/10/14)

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
        // done hase 戻り値はlongじゃなくて int  by jflute (2025/10/14)
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
