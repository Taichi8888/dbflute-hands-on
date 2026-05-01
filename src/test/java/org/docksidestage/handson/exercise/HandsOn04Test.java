package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.dbflute.exentity.Purchase;
import org.docksidestage.handson.unit.UnitContainerTestCase;

/**
 * セクション4のテストクラス
 * <ul>
 *     <li>退会会員の未払い購入を検索</li>
 *     <li>会員退会情報も取得して会員を検索</li>
 * </ul>
 * <ul>
 *     <li>一番若い仮会員の会員を検索</li>
 *     <li>支払済みの購入の中で一番若い正式会員のものだけ検索</li>
 *     <li>生産販売可能な商品の購入を検索</li>
 *     <li>正式会員と退会会員の会員を検索</li>
 *     <li>銀行振込で購入を支払ったことのある、会員ステータスごとに一番若い会員を検索</li>
 * </ul>
 *
 * @author hase
 */
public class HandsOn04Test extends UnitContainerTestCase {

    @Resource
    private PurchaseBhv purchaseBhv;
    @Resource
    private MemberBhv memberBhv;

    public void test_beta_selectPurchasePaymentNotCompletedByWithdrawal() throws Exception {
        // ## Arrange ##
        String statusCdWithdrawal = "WDL";
        int paymentNotCompleted = 0;

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            // done hase MEMBERテーブルがMEMBER_STATUS_CODEもってるから、queryMemberStatus()なくていい by jflute (2026/04/26)
//            cb.query().queryMember().queryMemberStatus().setMemberStatusCode_Equal(statusCdWithdrawal);
            cb.query().queryMember().setMemberStatusCode_Equal(statusCdWithdrawal);
            cb.query().setPaymentCompleteFlg_Equal(paymentNotCompleted);
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            String memberName = purchase.getMember().get().getMemberName();
            String productName = purchase.getProduct().get().getProductName();
            log(memberName, productName);
            assertEquals(paymentNotCompleted, purchase.getPaymentCompleteFlg());
        });
    }
    
    public void test__beta_selectMemberWithWithdrawal() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.setupSelect_MemberWithdrawalAsOne();
        });
    
        // ## Assert ##
        // #1on1: コードの横幅話。(2026/05/01)
        // 昔は、印刷幅で80の慣習があった。けど、もう印刷しないので...80は狭い。
        // jfluteは、140くらいにしている。フォーマッターの定義。
        // ハンズオンでは、Eclipseの設定もIntelliJの設定もgitコミットして共有している。

        // #1on1: IDEのフォーマッター設定 (2026/05/01)
        // フォーマッター設定は、全員で共有して使わないと意味がない。
        // もし個人個人で設定が違ったら、プルリクでフォーマッターの余計な差分が多過ぎ。
        // なので、共有というか強制というか、みんなで同じ設定を使いましょうという仕組みが必要。
        //
        // Eclipseの設定は、.settingsをgitコミットすれば良いだけ。
        // .settingsの中には個人設定は(基本的には)入らないので、そのままフォルダごとコミット。
        //
        // IntelliJの設定は、.ideaのgitコミットすれば良いというわけではない。
        // .ideaの中には個人設定も含まれるので、ファイルを取捨選択してgitコミットしないといけない。
        // なので、ピンポイントでgitコミットするgitignoreを設定する。
        // https://dbflute.seasar.org/ja/manual/topic/friends/intellij/index.html#gitignorestrategy
        //
        // 一方で、みんながフォーマッターを使わなければまた成立しない。
        // フォーマッターを掛ける人と掛けない人が混ざると、プルリクで大差分になっちゃう。
        // やるなら、みんな基本的には常に必ずフォーマッターを掛ける、じゃないと成り立たない。
        // 
        // Eclipseの場合、Save Actions で command+S (保存) のときに自動フォーマットできる。
        // その設定自体をgitコミットすれば良いだけ。
        //
        // IntelliJの場合、command+S (保存) のときに自動フォーマットする機能はあるんだけど...
        // その設定自体が.ideaの個人設定に入ってしまうので設定強制ができない。(みんなに働きかけるしかない)
        // 一方で、Save Actionsプラグインというのがあって、そっちだったら独立しているので設定強制ができる。
        // ただ、IntelliJは自動保存文化なので、command+S押さない人がいるので、それも成り立ちにくい。
        //
        // #1on1: IDEの違い、言語エンジンの話、開発組織の話 (2026/05/01)
        // 
        assertHasAnyElement(memberList);
        boolean hasAnyWithdrawal = memberList.stream().anyMatch(member -> member.getMemberStatus().get().getMemberStatusCode().equals("WDL"));
        assertTrue(hasAnyWithdrawal);

        memberList.forEach(member -> {
            member.getMemberStatus().alwaysPresent(status -> {
                if (status.getMemberStatusCode().equals("WDL")) {
                    assertTrue(member.getMemberWithdrawalAsOne().isPresent());
                } else {
                    assertFalse(member.getMemberWithdrawalAsOne().isPresent());
                }
            });
        });
    }

    public void test_selectPurchasePaymentNotCompletedByWithdrawal() throws Exception {
        // ## Arrange ##
        String statusCdWithdrawal = "WDL";
        int paymentNotCompleted = 0;

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            cb.query().queryMember().setMemberStatusCode_Equal(statusCdWithdrawal);
            cb.query().setPaymentCompleteFlg_Equal(paymentNotCompleted);
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            String memberName = purchase.getMember().get().getMemberName();
            String productName = purchase.getProduct().get().getProductName();
            log(memberName, productName);
            assertEquals(paymentNotCompleted, purchase.getPaymentCompleteFlg());
        });
    }
}
