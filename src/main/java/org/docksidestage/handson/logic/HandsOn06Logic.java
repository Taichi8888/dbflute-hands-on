package org.docksidestage.handson.logic;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandsOn06Logic {

    private static final Logger logger = LoggerFactory.getLogger(HandsOn06Logic.class);

    @Resource
    private MemberBhv memberBhv;

    public ListResultBean<Member> selectSuffixMemberList(String suffix) {
        if (suffix == null || suffix.trim().isEmpty()) {
            throw new IllegalArgumentException("The suffix should not be null or empty: " + suffix);
        }
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().setMemberName_LikeSearch(suffix, op -> op.likeSuffix());
            cb.query().addOrderBy_MemberName_Asc();
        });
        memberList.forEach(member -> {
            logger.debug("memberName={}, birthdate={}, formalizedDatetime={}",
                    member.getMemberName(), member.getBirthdate(), member.getFormalizedDatetime());
        });
        return memberList;
    }
}
