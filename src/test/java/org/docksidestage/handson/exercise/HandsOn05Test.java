package org.docksidestage.handson.exercise;

import java.time.LocalDate;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.allcommon.CDef;
import org.docksidestage.handson.dbflute.exbhv.MemberAddressBhv;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.PurchaseBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.dbflute.exentity.MemberAddress;
import org.docksidestage.handson.dbflute.exentity.MemberLogin;
import org.docksidestage.handson.dbflute.exentity.Purchase;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// #1on1: 業務的one-to-one とは？ (2026/06/02)
// ちょこっとだけ応用編の紹介も。いずれしっかり。

// #1on1: 現場の業務的one-to-oneも見てみた (2026/06/19)
// OverRelationの例も。
public class HandsOn05Test extends UnitContainerTestCase {

    @Resource
    private MemberAddressBhv memberAddressBhv;
    @Resource
    private MemberBhv memberBhv;
    @Resource
    private PurchaseBhv purchaseBhv;

    public void test_selectMemberAddress() throws Exception {
        // ## Arrange ##
        
        
        // ## Act ##
        ListResultBean<MemberAddress> memberAddressList = memberAddressBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Region();
            cb.query().addOrderBy_MemberId_Asc();
            cb.query().addOrderBy_ValidBeginDate_Desc();
        });
    
        // ## Assert ##
        assertHasAnyElement(memberAddressList);
        memberAddressList.forEach(memberAddress -> {
            log(memberAddress.getMember().orElseThrow().getMemberName(), memberAddress.getValidBeginDate(), memberAddress.getValidEndDate(), memberAddress.getAddress(), memberAddress.getRegion().orElseThrow().getRegionName());
        });
    }
    
    public void test_selectMemberWithCurrentAddress() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberAddressAsValid(currentLocalDate());
            cb.query().addOrderBy_MemberId_Asc();
        });
    
        // ## Assert ##
        assertHasAnyElement(memberList);
        assertTrue(memberList.stream().anyMatch(m -> m.getMemberAddressAsValid().isPresent()));
        memberList.forEach(member -> {
            member.getMemberAddressAsValid().ifPresent(address -> {
                log(member.getMemberName(), address.getAddress());
            });
        });
    }
    
    public void test_selectPurchaseByMemberLivingInChiba() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberStatus();
            cb.setupSelect_Member().withMemberAddressAsValid(currentLocalDate());
            cb.query().queryMember().queryMemberAddressAsValid(currentLocalDate()).setRegionId_Equal_千葉();
            cb.query().addOrderBy_PurchaseId_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            Member member = purchase.getMember().orElseThrow();
            MemberAddress memberAddress = member.getMemberAddressAsValid().orElseThrow();
            log(member.getMemberStatus().orElseThrow().getMemberStatusName(), memberAddress.getAddress());
            assertTrue(memberAddress.isRegionId千葉());
        });
    }
    
    public void test_selectMemberWithMemberStatusOfLastLogin() throws Exception {
        // ## Arrange ##
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberLoginAsLatest().withMemberStatus();
            cb.query().addOrderBy_MemberId_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        assertTrue(memberList.stream().anyMatch(m -> m.getMemberLoginAsLatest().isPresent()));
        memberList.forEach(member -> {
            member.getMemberLoginAsLatest().ifPresent(login -> {
                log(member.getMemberName(), login.getLoginDatetime(), login.getMemberStatus().orElseThrow().getMemberStatusName());
            });
        });
    }
    
    // #1on1: take-finally.sqlのお話 (2026/06/19)
    // テストデータの品質を高めよう。
    // 現場のtake-finallyも見てみた。昔のものでは今すごく効果がある。
    // 一方で、もっと気軽に増やしていいのに。
    // 今だったらAIでSQL書いてもらえるから、もっと簡単に追加できるはず。
    // take-finallyの作るきっかけ話。
    
    // #1on1: ConditionBeanスタイルのお話 (2026/06/19)
    // 実は意外にメジャーだった？
    // https://www.sqlstyle.guide/ja/
    // SQLのフォーマットの現実問題の話。
    // よほどのこだわりがなければ、ぜひConditionBeanスタイル。
    // それで多くのSQLのフォーマットが統一されればみやすくなる。
    // 大文字小文字の話も。
}
