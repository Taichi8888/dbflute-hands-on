package org.docksidestage.handson.exercise;

import java.time.LocalDate;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// #1on1: いいね、JavaDocがわかりやすい。 (2025/11/28)
/**
 * セクション3のテストクラス
 * <ul>
 *     <li>会員名称がSで始まる1968年1月1日以前に生まれた会員を検索</li>
 *     <li>会員ステータスと会員セキュリティ情報も取得して会員を検索</li>
 *     <li>会員セキュリティ情報のリマインダ質問で2という文字が含まれている会員を検索</li>
 * </ul>
 *
 * @author hase
 */
public class HandsOn03Test extends UnitContainerTestCase {

    @Resource
    private MemberBhv memberBhv;

    public void test_selectMemberStartsWithSBornBefore1968() throws Exception {
        // ## Arrange ##
        String prefix = "S";
        // TODO hase プログラムの世界でDayだと、日数とか日だけを指すニュアンスになることが多い by jflute (2025/11/28)
        // ここでは日付、年月日なので、Dateの方が誤解が少ない。renameお願いします。
        LocalDate xDay = toLocalDate("1968-01-01");

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.query().setMemberName_LikeSearch(prefix, op -> op.likePrefix());
            // #1on1: 日付の言葉の曖昧さについて (2025/11/28)
            // TODO hase LocalDateをもっかいLocalDateにしている by jflute (2025/11/28)
            cb.query().setBirthdate_LessEqual(toLocalDate(xDay));
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            // TODO hase 変数抽出を by jflute (2025/11/28)
            log(member.getMemberName() + ", " + member.getBirthdate());
            // TODO hase ここ、getMemberStatus(), アサートが成り立ってないです。 by jflute (2025/11/28)
            // setupSelectを一時的にコメントアウトしても落ちないのはなぜ？
            assertNotNull(member.getMemberStatus());
            assertTrue(member.getMemberName().startsWith(prefix));
            assertFalse(member.getBirthdate().isAfter(toLocalDate(xDay)));
        }
    }
    
    public void test_selectMemberWithStatusAndSecurity() throws Exception {
        // ## Arrange ##
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.setupSelect_MemberSecurityAsOne();
            cb.query().addOrderBy_Birthdate_Desc();
            cb.query().addOrderBy_MemberId_Asc();
        });
    
        // ## Assert ##
        // #1on1: さて、絞り込みもなくすべての会員を取っていますが...
        // 会員ステータスは、すべての会員で存在するといい切れるのでしょうか？
        // 会員セキュリティは、すべての会員で存在するといい切れるのでしょうか？
        // (いまの実装は、そのようになっていますが、それを裏付けるものは何？)
        //
        // 会員ステータス:
        //  → not nullでFK制約が貼られているから、探しにいけば絶対にあるといいきれる
        // 会員セキュリティ:
        //  → 物理的な制約はないけど、業務的にはERDの黒丸で必ず存在することになってる
        //  → あと、DBコメントに「会員一人につき必ず一つのセキュリティ情報がある」と書いてある
        //
        // 会員から会員ステータスは、NotNullのFKカラムで参照しているので、探しにいけば必ず存在する
        // 会員から会員セキュリティは、FKの方向と探しにいく方向が逆なので同じ理論にはなりませんが、
        // ERDのリレーションシップ線に注目。会員退会情報と比べると一目瞭然、黒丸がついていないので必ず存在する1
        //   会員から会員セキュリティ => 1:必ず1 (1:1)
        //   会員から会員退会情報    => 1:いないかもしれない1 (1:0..1)
        // ただ、物理的な制約はありません。業務的というのは、そういうルールにしているいうことだけなんですね。
        // 細かいですが、これがデータベースプログラミングにおいて、とても重要なんですよね。
        // ぜひ、カージナリティに着目してみてください。
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            assertTrue(member.getMemberStatus().isPresent());
            assertTrue(member.getMemberSecurityAsOne().isPresent());
        });
        // TODO hase [読み物課題] コードにコメント書くのにDBにコメント書かないの？ by jflute (2025/11/28)
        // https://jflute.hatenadiary.jp/entry/20170628/letsdbcomment
    }
    // TODO jflute 次回1on1ここから (2025/11/28)

    public void test_selectMemberWithSecurityQuestionContaining2() throws Exception {
        // ## Arrange ##
        String wordIncluded = "2";

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberSecurityAsOne();
            cb.query().queryMemberSecurityAsOne().setReminderQuestion_LikeSearch(wordIncluded, op -> op.likeContain());
            cb.query().addOrderBy_MemberId_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            String question = member.getMemberSecurityAsOne().get().getReminderQuestion();
            log(member.getMemberName() + ", " + question);
            assertTrue(question.contains(wordIncluded));
        }
    }
}
