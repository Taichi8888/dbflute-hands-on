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
            // TODO done hase MEMBERテーブルがMEMBER_STATUS_CODEもってるから、queryMemberStatus()なくていい by jflute (2026/04/26)
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
