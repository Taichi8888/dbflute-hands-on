package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.dbflute.jdbc.ClassificationUndefinedHandlingType;
import org.docksidestage.handson.dbflute.allcommon.CDef;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.dbflute.exentity.Purchase;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// #1on1: dfpropの$sqlのお話。自己参照の階層構造になってるようなものに使える (2026/05/08)
// #1on1: アプリ区分値のお話。ロジック区分値とも言う (2026/05/08)
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

    // TODO hase タグコメントをちょっと入れてみてください by jflute (2026/05/08)
    
    public void test_beta_selectPurchasePaymentNotCompletedByWithdrawal() throws Exception {
        // ## Arrange ##
        String statusCdWithdrawal = "WDL";
        int paymentNotCompleted = 0;

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            // done hase MEMBERテーブルがMEMBER_STATUS_CODEもってるから、queryMemberStatus()なくていい by jflute (2026/04/26)
            // cb.query().queryMember().queryMemberStatus().setMemberStatusCode_Equal(statusCdWithdrawal);
// 区分値定義追加でコンパイルエラーになったのでコメントアウト by hase（2026/5/7）
            //            cb.query().queryMember().setMemberStatusCode_Equal(statusCdWithdrawal);
            //            cb.query().setPaymentCompleteFlg_Equal(paymentNotCompleted);
//
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

    public void test_beta_selectMemberWithWithdrawal() throws Exception {
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
        boolean hasAnyWithdrawal =
                memberList.stream().anyMatch(member -> member.getMemberStatus().get().getMemberStatusCode().equals("WDL"));
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

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            cb.query().queryMember().setMemberStatusCode_Equal_退会会員();
            // TODO hase falseのメソッドがあるのでそっちを by jflute (2026/05/08)
            cb.query().setPaymentCompleteFlg_Equal_AsFlg(CDef.Flg.False);
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            Member member = purchase.getMember().get();
            String memberName = member.getMemberName();
            String productName = purchase.getProduct().get().getProductName();
            log(memberName, productName);
            assertTrue(member.isMemberStatusCode退会会員());
            assertTrue(purchase.isPaymentCompleteFlgFalse());
        });
    }

    public void test_selectMemberWithWithdrawal() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.setupSelect_MemberWithdrawalAsOne();
        });

        // ## Assert ##

        assertHasAnyElement(memberList);
        boolean hasAnyWithdrawal =
                memberList.stream().anyMatch(member -> member.isMemberStatusCode退会会員());
        assertTrue(hasAnyWithdrawal);

        memberList.forEach(member -> {
            if (member.isMemberStatusCode退会会員()) {
                assertTrue(member.getMemberWithdrawalAsOne().isPresent());
            } else {
                assertFalse(member.getMemberWithdrawalAsOne().isPresent());
            }
        });
    }

    public void test_selectMemberYoungestProvisional() throws Exception {
        // ## Arrange ##

        // ## Act ##
        // #1on1: 同率首位がいることを想定できてるのGood (2026/05/08)
        // 現状の実装だと、ランダムで1件を特定している。
        // もう一個は、同率首位をみんな取得する。
        Member member = memberBhv.selectEntity(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().specifyMemberStatus().columnMemberStatusName();
            cb.query().setMemberStatusCode_Equal_仮会員();
            // TODO hase 第二ソートキーでfetch1を固定化した方が良い by jflute (2026/05/08)
            //  Aさん 2026/05/08
            //  Bさん 2026/05/08
            // いまこの二人をどう並べるかは指定されていないので、論理的にはランダムになる。
            // すると、検索するたびに、AさんだったりBさんだったりブレる可能性がある。
            // なので、要件にはないけど、第二ソートキーでユニークに並べるようにした方がいい。
            cb.query().addOrderBy_Birthdate_Desc();
            cb.fetchFirst(1);
        }).get();
        
        // ## Assert ##
        log(member.getMemberName(), member.getBirthdate(), member.getMemberStatus().get().getMemberStatusName());
        assertTrue(member.isMemberStatusCode仮会員());
        
        // TODO hase ちょっと同率首位を検索するのも以下に追加で書いてみましょう by jflute (2026/05/08)
    }

    public void test_selectPurchaseByYoungestFormalizedMember() throws Exception {
        // ## Arrange ##


        // ## Act ##
        // #1on1: 一応、できてる。支払い済み購入が存在する会員の中で一番若い人を取ってるので合ってる (2026/05/08)
        // TODO hase DBFluteの機能を使ってSQLを一回で済ませたい。 by jflute (2026/05/08)
        // (現状の実装は、これはこれで思い出として残して)
        // (先に、仮会員の同率首位をみんな検索するやつを先にやってみた方が良い)
        Member youngestMember = purchaseBhv.selectEntity(cb -> {
            cb.setupSelect_Member();
            cb.query().setPaymentCompleteFlg_Equal_True();
            cb.query().queryMember().setMemberStatusCode_Equal_正式会員();
            cb.query().queryMember().addOrderBy_Birthdate_Desc();
            cb.fetchFirst(1);
        }).get().getMember().get();

        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberStatus();
            cb.specify().specifyMember().specifyMemberStatus().columnMemberStatusName();
            cb.query().queryMember().setMemberId_Equal(youngestMember.getMemberId());
            cb.query().setPaymentCompleteFlg_Equal_True();
            cb.query().addOrderBy_PurchaseDatetime_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            Member member = purchase.getMember().get();
            log(member.getMemberName(), member.getMemberStatus().get().getMemberStatusName(), purchase.getPurchaseDatetime());
            assertTrue(member.isMemberStatusCode正式会員());
        });
    }
}
