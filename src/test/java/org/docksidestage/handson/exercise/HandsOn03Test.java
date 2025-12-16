package org.docksidestage.handson.exercise;

import java.time.LocalDate;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
import org.docksidestage.handson.dbflute.exbhv.MemberBhv;
import org.docksidestage.handson.dbflute.exbhv.MemberSecurityBhv;
import org.docksidestage.handson.dbflute.exentity.Member;
import org.docksidestage.handson.unit.UnitContainerTestCase;

/**
 * セクション3のテストクラス
 * <ul>
 *     <li>会員名称がSで始まる1968年1月1日以前に生まれた会員を検索</li>
 *     <li>会員ステータスと会員セキュリティ情報も取得して会員を検索</li>
 *     <li>会員セキュリティ情報のリマインダ質問で2という文字が含まれている会員を検索</li>
 * </ul>
 * <ul>
 *     <li>会員ステータスの表示順カラムで会員を並べて検索</li>
 *     <li>生年月日が存在する会員の購入を検索</li>
 *     <li>2005年10月の1日から3日までに正式会員になった会員を検索</li>
 * </ul>
 *
 * @author hase
 */
public class HandsOn03Test extends UnitContainerTestCase {

    @Resource
    private MemberBhv memberBhv;
    @Resource
    private MemberSecurityBhv memberSecurityBhv;

    public void test_selectMemberStartsWithSBornBefore1968() throws Exception {
        // ## Arrange ##
        String prefix = "S";
        LocalDate xDay = toLocalDate("1968-01-01");

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.query().setMemberName_LikeSearch(prefix, op -> op.likePrefix());
            cb.query().setBirthdate_LessEqual(toLocalDate(xDay));
            cb.query().addOrderBy_MemberName_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        for (Member member : memberList) {
            log(member.getMemberName() + ", " + member.getBirthdate());
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
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            assertTrue(member.getMemberStatus().isPresent());
            assertTrue(member.getMemberSecurityAsOne().isPresent());
        });
    }

    public void test_selectMemberWithSecurityQuestionContaining2() throws Exception {
        // ## Arrange ##
        String wordIncluded = "2";

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.query().queryMemberSecurityAsOne().setReminderQuestion_LikeSearch(wordIncluded, op -> op.likeContain());
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            memberSecurityBhv.selectByPK(member.getMemberId()).alwaysPresent(security -> {
                String question = security.getReminderQuestion();
                log(member.getMemberName(), question);
                assertTrue(question.contains(wordIncluded));
            });
        });
    }
}
