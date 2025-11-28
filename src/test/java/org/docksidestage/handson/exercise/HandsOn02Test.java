package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
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
// done hase [読み物課題] 別にパソコンがなくてもプログラミングはできるよ by jflute (2025/10/29)
// https://jflute.hatenadiary.jp/entry/20170923/nopcpg

// TODO jflute いつか1on1で、IntelliJの.ideaのお話 (2025/10/14)
// https://dbflute.seasar.org/ja/tutorial/handson/section01.html#ideashared

// done hase DBFluteクライアントのlogのログファイルをgitignoreにお願いします by jflute (2025/10/14)
// 引用:
// ハンズオンをどこかの Git で管理する場合は、logファイルを .gitignore に登録しておきましょう。
// (logディレクトリに.gitignoreファイルを作成し、*.logと指定しておくでOKです)
// done hase 既存のコミット済みのものはそのままなので削除コミットしましょう by jflute (2025/10/29)
// (.gitignoreのニュアンス、今後コミットするときにignoreにするって感じ)
// #1on1: .gitignore戦略の話 (設定ファイルを綺麗に、階層をうまく使う)

// done hase javadocお願い by jflute (2025/10/14)

// #1on1: 例外翻訳の実践 (2025/11/14)
// Field 'REGISTER_DATETIME' doesn't have a default value
// *And also check the insert values to not-null columns.
// A     ->    B    ->     C     ->     D 全部大事
// この論理がわかれば全部読もうとするはず。

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

    public void test_selectMemberStartsWithS() throws Exception {
        // ## Arrange ##
        // #1on1: "S" を変数に出すか出さないか？UnitTestの実装ポリシー次第。(現場によって変わる)
        // 個人的には、わりと変数に出して抽象化はして、実験とかしやすいようにするけど...

        // ## Act ##
        // #1on1: IntelliJでの補完を想定したライブコーディング (2025/11/14)
        // TODO done hase キャメルケースコード補完 by jflute (2025/11/14)
        // https://dbflute.seasar.org/ja/manual/topic/programming/completion/camelcase.html
        // あと、戻り値の補完、IntelliJだと .var
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setMemberName_LikeSearch("S", op -> op.likePrefix());
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        // TODO hase assertHasAnyElement()というメソッド用意してるのでそっち使ってみてください by jflute (2025/11/14)
        //  e.g. assertHasAnyElement(memberList);
        // assH + 補完でenter で、memberList を入れる
        //assertFalse(memberList.isEmpty());
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            log(member.getMemberName());
            assertTrue(member.getMemberName().startsWith("S"));
        }
    }

    /**
     * 会員IDが1の会員を検索
     */
    public void test_selectMemberById() throws Exception {
        // ## Arrange ##

        // ## Act ##
        // #1on1: javatryと合わせて、Optionalの根幹となる機能のお話 (2025/11/14)
        // 根幹にプラスして、実装しやすさの演出をするメソッドたち e.g. ifPresent(), map()
        //
        // #1on1: UnitTestとしては、1がなかったら論外なので、問答無用get()しちゃっている (2025/11/14)
        // 一方で、Optionalで問答無用get()はあまり良くないと言われている。
        // ただ、データベースの検索においては、条件値によって「絶対あるよ、なけりゃ落ちていい」って状況がある。
        // 例えば、一覧画面で絶対に存在することが保証されたIDを受け取って詳細検索するってとき...
        // (すれ違いのときは例外で落ちていいみたいとき)
        // なので、DBFluteでは、その状況が正しく把握できていれば、時に問答無用get()でも良いとしている。
        // 
        // もちろん、get()じゃなくて、orElseThrow() を常に使いましょうというのは正論ではある。
        // get()で落ちたら NoSuchElementException ということで NullPo と情報量変わらない。
        // TODO jflute 1on1続き (2025/11/14)
        Member member = memberBhv.selectEntity(cb -> {
            cb.query().setMemberId_Equal(1);
        }).get();

        // ## Assert ##
        log(member.getMemberName());
        assertEquals(1, member.getMemberId());
    }
    
    public void test_selectMemberNoBirthdate() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setBirthdate_IsNull();
            cb.query().addOrderBy_UpdateDatetime_Desc();
        });

        // ## Assert ##
        // assertFalse(memberList.isEmpty());
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            log(member.getMemberName());
            assertNull(member.getBirthdate());
        }
    }

}
