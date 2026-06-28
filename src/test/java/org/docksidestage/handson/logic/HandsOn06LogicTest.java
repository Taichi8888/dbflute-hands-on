package org.docksidestage.handson.logic;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.unit.UnitContainerTestCase;

public class HandsOn06LogicTest extends UnitContainerTestCase {

    public void test_selectSuffixMemberList_指定したsuffixで検索されること() throws Exception {
        // ## Arrange ##
        HandsOn06Logic logic = new HandsOn06Logic();
        inject(logic);
        String suffix = "vic";

        // ## Act ##
        ListResultBean<Member> memberList = logic.selectSuffixMemberList(suffix);

        // ## Assert ##
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            log(member.getMemberName(), member.getBirthdate(), member.getFormalizedDatetime());
            assertTrue(member.getMemberName().endsWith(suffix));
        });
    }

    public void test_selectSuffixMemberList_suffixが無効な値なら例外が発生すること() throws Exception {
        // ## Arrange ##
        HandsOn06Logic logic = new HandsOn06Logic();
        inject(logic);

        // ## Act & Assert ##
        assertException(IllegalArgumentException.class, () -> logic.selectSuffixMemberList(null));
        assertException(IllegalArgumentException.class, () -> logic.selectSuffixMemberList(""));
        assertException(IllegalArgumentException.class, () -> logic.selectSuffixMemberList("   "));
    }
}
