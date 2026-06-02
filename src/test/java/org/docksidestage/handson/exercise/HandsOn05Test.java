package org.docksidestage.handson.exercise;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberAddressBhv;
import org.docksidestage.handson.dbflute.exentity.MemberAddress;
import org.docksidestage.handson.unit.UnitContainerTestCase;

// #1on1: 業務的one-to-one とは？ (2026/06/02)
// ちょこっとだけ応用編の紹介も。いずれしっかり。

public class HandsOn05Test extends UnitContainerTestCase {

    @Resource
    private MemberAddressBhv memberAddressBhv;

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
}
