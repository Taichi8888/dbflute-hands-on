package org.docksidestage.handson.exercise;

import java.util.Arrays;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.dbflute.cbean.result.ListResultBean;
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

    // done hase タグコメントをちょっと入れてみてください by jflute (2026/05/08)
    // ===================================================================================
    //                                                            区分値使わずベタベタ実装 2題
    //                                                            ========================
    public void test_beta_selectPurchasePaymentNotCompletedByWithdrawal() throws Exception {
        // ## Arrange ##
        @SuppressWarnings("unused")
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

    // ===================================================================================
    //                                                         区分値使ってスマート実装 同じ2題
    //                                                         ===========================
    public void test_selectPurchasePaymentNotCompletedByWithdrawal() throws Exception {
        // ## Arrange ##

        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member();
            cb.setupSelect_Product();
            cb.query().queryMember().setMemberStatusCode_Equal_退会会員();
            // done hase falseのメソッドがあるのでそっちを by jflute (2026/05/08)
            //            cb.query().setPaymentCompleteFlg_Equal_AsFlg(CDef.Flg.False);
            cb.query().setPaymentCompleteFlg_Equal_False();
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
        boolean hasAnyWithdrawal = memberList.stream().anyMatch(member -> member.isMemberStatusCode退会会員());
        assertTrue(hasAnyWithdrawal);

        memberList.forEach(member -> {
            if (member.isMemberStatusCode退会会員()) {
                assertTrue(member.getMemberWithdrawalAsOne().isPresent());
            } else {
                assertFalse(member.getMemberWithdrawalAsOne().isPresent());
            }
        });
    }

    // ===================================================================================
    //                                                                 区分値メソッド演習 5題
    //                                                                 ===================
    public void test_selectMemberYoungestProvisional() throws Exception {
        // ## Arrange ##

        // ## Act ##
        // #1on1: 同率首位がいることを想定できてるのGood (2026/05/08)
        // 現状の実装だと、ランダムで1件を特定している。
        // もう一個は、同率首位をみんな取得する。
        Member youngestProvisional = memberBhv.selectEntity(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().specifyMemberStatus().columnMemberStatusName();
            cb.query().setMemberStatusCode_Equal_仮会員();
            // done hase 第二ソートキーでfetch1を固定化した方が良い by jflute (2026/05/08)
            //  Aさん 2026/05/08
            //  Bさん 2026/05/08
            // いまこの二人をどう並べるかは指定されていないので、論理的にはランダムになる。
            // すると、検索するたびに、AさんだったりBさんだったりブレる可能性がある。
            // なので、要件にはないけど、第二ソートキーでユニークに並べるようにした方がいい。
            cb.query().addOrderBy_Birthdate_Desc(); // (機能要件)
            cb.query().addOrderBy_MemberId_Asc(); // 検索結果固定化のために (非機能要件)
            cb.fetchFirst(1);
        }).get();

        // ## Assert ##
        log(youngestProvisional.getMemberName(), youngestProvisional.getBirthdate(),
                youngestProvisional.getMemberStatus().get().getMemberStatusName());
        assertTrue(youngestProvisional.isMemberStatusCode仮会員());

        // done hase ちょっと同率首位を検索するのも以下に追加で書いてみましょう by jflute (2026/05/08)
        // 「導出値との比較で絞り込み」https://dbflute.seasar.org/ja/manual/function/genbafit/implfit/subquery/index.html by hase

        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.specify().specifyMemberStatus().columnMemberStatusName();
            cb.query().setMemberStatusCode_Equal_仮会員();
            // done hase cb2 をどうにかしたい。文字数を変えておきたい。JavaDocに合わせましょう by jflute (2026/05/12)
            // #1on1: 昔はsubCB慣習だった話。でもLambdaになって隠蔽変数できなくなった話。
            cb.query().scalar_Equal().max(memberCB -> {
                memberCB.specify().columnBirthdate();
                memberCB.query().setMemberStatusCode_Equal_仮会員();
            });
            cb.query().addOrderBy_MemberId_Asc();
        });

        // ## Assert ##
        assertHasAnyElement(memberList);
        memberList.forEach(member -> {
            log(member.getMemberName(), member.getBirthdate(), member.getMemberStatus().get().getMemberStatusName());
            assertTrue(member.isMemberStatusCode仮会員());
        });

    }

    public void test_selectPurchaseByYoungestFormalizedMember() throws Exception {
        // ## Arrange ##

        // ## Act ##
        // #1on1: 一応、できてる。支払い済み購入が存在する会員の中で一番若い人を取ってるので合ってる (2026/05/08)
        // done hase DBFluteの機能を使ってSQLを一回で済ませたい。 by jflute (2026/05/08)
        // (現状の実装は、これはこれで思い出として残して)
        // (先に、仮会員の同率首位をみんな検索するやつを先にやってみた方が良い)

// おもいで
//        Member youngestMember = purchaseBhv.selectEntity(cb -> {
//            cb.setupSelect_Member();
//            cb.query().setPaymentCompleteFlg_Equal_True();
//            cb.query().queryMember().setMemberStatusCode_Equal_正式会員();
//            cb.query().queryMember().addOrderBy_Birthdate_Desc();
//            cb.fetchFirst(1);
//        }).get().getMember().get();
//
//        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
//            cb.setupSelect_Member().withMemberStatus();
//            cb.specify().specifyMember().specifyMemberStatus().columnMemberStatusName();
//            cb.query().queryMember().setMemberId_Equal(youngestMember.getMemberId());
//            cb.query().setPaymentCompleteFlg_Equal_True();
//            cb.query().addOrderBy_PurchaseDatetime_Desc();
//        });

        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberStatus();
            cb.specify().specifyMember().specifyMemberStatus().columnMemberStatusName();
            cb.query().setPaymentCompleteFlg_Equal_True();
            cb.query().queryMember().setMemberStatusCode_Equal_正式会員();
            // TODO done hase cb2まだいた (全体検索漏れ) by jflute (2026/05/19)
            // 再び "指摘されたら、似たようなところが他にもないか探す" 習慣を
            cb.query().queryMember().scalar_Equal().max(memberCB -> {
                memberCB.specify().columnBirthdate();
                memberCB.query().setMemberStatusCode_Equal_正式会員();
                // done hase purchaseCb ではなく purchaseCB (ただの慣習) by jflute (2026/05/12)
                memberCB.query().existsPurchase(purchaseCB -> {
                    purchaseCB.query().setPaymentCompleteFlg_Equal_True();
                });
            });
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
    
    public void test_selectPurchaseOfProductOnSupport() throws Exception {
        // ## Arrange ##
        
        
        // ## Act ##
        ListResultBean<Purchase> purchaseList = purchaseBhv.selectList(cb -> {
            cb.setupSelect_Member().withMemberWithdrawalAsOne().withWithdrawalReason();
            cb.setupSelect_Product().withProductStatus();
            cb.query().queryProduct().setProductStatusCode_Equal_生産販売可能();
            cb.query().addOrderBy_PurchasePrice_Desc();
        });

        // ## Assert ##
        assertHasAnyElement(purchaseList);
        purchaseList.forEach(purchase -> {
            // #1on1: javatryのstep8の復習 (2026/05/12)
            // DBFluteのOptional利用の特徴として...ハイブリッド
            //  o 関連テーブル: Optional (関連データがあるか？setupSelectしたか？)
            //  o カラム: null (データがあるか？(NotNull制約か？) specifyしたか？)
            // カラムの特徴から来る、OptionalのorElseThrow()ジレンマの嵐から、
            // カラムのOptional採用は見送った。採用しても理想のOptionalにならないので。
            // Optional採用するにしても、NotNull制約のカラムはどうする問題。
            // (NotNull制約の有無で、簡単に自動生成コードが変わらないようにしている)
            //
            // DB設計のNotNull制約の勉強もした。
            String reasonText = purchase.getMember()
                    .flatMap(member -> member.getMemberWithdrawalAsOne())
                    .flatMap(withdrawal -> withdrawal.getWithdrawalReason())
                    .map(reason -> reason.getWithdrawalReasonText())
                    .orElse("none");
            // done hase これは絶対に存在する場面なので、orElseThrow(引数なし)とか使うでいいかと by jflute (2026/05/12)
            // e.g. ... = purchase.getProduct().orElseThrow().getProductStatus().orElseThrow().getProductStatusName();
            String statusName = purchase.getProduct().orElseThrow()
                    .getProductStatus().orElseThrow()
                    .getProductStatusName();
// おもいで            String statusName = purchase.getProduct()
//                    .flatMap(product -> product.getProductStatus())
//                    .map(status -> status.getProductStatusName())
//                    .orElse("none"); // 勉強用メモ：必ず値があるのがわかっているのにflatmapは良くないのか？by hase
            // #1on1: orElseThrow()ジレンマと同じ。業務的に必ず存在するのに、ないかもしれないことを想定した実装する？
            // まず、必ず値があるのがわかっているのであれば、デフォルト値は使わない方が良い。紛らわしい。
            // noneがありえるのかな？と思ってしまう。
            // じゃあやっぱり、教科書通りのorElseThrow()で例外にするのか？
            // した方がいいけど、こういった関連テーブルとかだとしょっちゅうこういう場面があって面倒。
            // ちゃんとしたorElseThrow()で例外メッセージを入れる人も少ないかもしれない。
            // なので、DBFluteのOptionalではalwaysPresent()とかリッチ例外付きのメソッドがある。
            log(reasonText, statusName, purchase.getPurchasePrice());
            assertTrue(purchase.getProduct().get().isProductStatusCode生産販売可能());
        });
    }
    
    public void test_selectMemberFormalizedOrWithdrawal() throws Exception {
        // ## Arrange ##
        
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.query().setMemberStatusCode_InScope_AsMemberStatus(Arrays.asList(CDef.MemberStatus.正式会員, CDef.MemberStatus.退会会員));
            cb.query().queryMemberStatus().addOrderBy_DisplayOrder_Asc();
        });
    
        // ## Assert ##
        assertHasAnyElement(memberList.stream().filter(member -> member.isMemberStatusCode正式会員()).collect(Collectors.toList()));
        assertHasAnyElement(memberList.stream().filter(member -> member.isMemberStatusCode退会会員()).collect(Collectors.toList()));
        memberList.forEach(member -> { // e.g. 20
            String statusName = member.getMemberStatus() // e.g. n(=20)+1 SQL count if lazy load
                    .map(status -> status.getMemberStatusName())
                    .orElse("none");
            log(statusName, member.getMemberName());
            assertTrue(member.isMemberStatusCode正式会員() || member.isMemberStatusCode退会会員());
        });
        Member formalizedMember = memberList.stream().filter(member -> member.isMemberStatusCode正式会員()).findFirst().get();
        log(formalizedMember.getMemberStatusCode(), formalizedMember.getMemberName());
        formalizedMember.setMemberStatusCodeAsMemberStatus(CDef.MemberStatus.退会会員);
        log(formalizedMember.getMemberStatusCode(),formalizedMember.getMemberStatus().get().getMemberStatusName(), formalizedMember.getMemberName());
        assertTrue(formalizedMember.isMemberStatusCode退会会員());
        assertTrue(memberBhv.selectEntity(cb -> {
            cb.query().setMemberId_Equal(formalizedMember.getMemberId());
        }).get().isMemberStatusCode正式会員());

        // #1on1: DBFluteは、メモリ上(Entityクラス)だけを修正しても、DBに勝手にupdateとかは掛からない (2026/05/19)
        // あくまで、Behaviorで明示的に update とかしない限りDBは変わらない。あえてそうしている。
        //
        // 他のO/Rマッパーでは、Entityにsetしたら、それだけでupdateがされるものもある。
        // EntityがDBと直結しているかのような...もしくは、Entity自体がDBのような...
        // RDBを隠蔽して、オブジェクトDBみたいな感覚で扱うO/Rマッパー。
        //
        // DBFluteは、SQLをラップはしているけど、RDBは意識している。
        // RDB意識のO/Rマッパー、RDB隠蔽のO/Rマッパー。
        //
        // DBFluteは、JPAをimplementsしていない。
        // JPAは、RDB隠蔽のO/Rマッパーコンセプトのインターフェースなので合わない。
        
        // #1on1: DBFluteは、LazyLoadは(あえて)提供しない。 (2026/05/19)
        // n+1問題が発生しやすい機能ということで避けている。
        // n+1だとなぜ遅くなるのか？話。
        // jfluteの経験では、1回のリクエストで300〜500のSQLを発行しているケースみたことある。
    }
    
    public void test_selectMemberYoungestOfEachStatusWithBankTransfer() throws Exception {
        // ## Arrange ##
        
        
        // ## Act ##
        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
            cb.setupSelect_MemberStatus();
            cb.query().arrangePaidByBankTransfer();
            // #1on1: partitionByのお話し (2026/05/19)
            // #1on1: ArrangeQueryでCBの一部記述を部品化できる (2026/05/19)
            // まず最小公倍数パターンのお話。無駄なデータをみんなで取りまくる。(可読性も悪い)
            // そしてコピペパターン。まるごとコピーして、select...2()
            // でもってそれらを避けようとして引数リモコンパターン。
            // 現場のArrangeQuery。
            cb.query().scalar_Equal().max(memberCB -> {
                memberCB.specify().columnBirthdate();
                memberCB.query().arrangePaidByBankTransfer();
            }).partitionBy(colCB -> colCB.specify().columnMemberStatusCode());
            cb.query().addOrderBy_MemberStatusCode_Asc();
        });
    
        // ## Assert ##
        assertTrue(memberList.size() >= 3);
        assertHasAnyElement(memberList.stream().filter(m -> m.isMemberStatusCode正式会員()).collect(Collectors.toList()));
        assertHasAnyElement(memberList.stream().filter(m -> m.isMemberStatusCode仮会員()).collect(Collectors.toList()));
        assertHasAnyElement(memberList.stream().filter(m -> m.isMemberStatusCode退会会員()).collect(Collectors.toList()));
        memberList.forEach(member -> {
            log(member.getMemberStatus().orElseThrow().getMemberStatusName(), member.getMemberName(), member.getBirthdate());
        });
    }

//    区分値の追加を検証時に使用したテスト。元に戻してコンパイルエラーになることを確認したため、コメントアウト。by hase (2026/05/24)
//    public void test_selectMemberIsHandson() throws Exception {
//        // ## Arrange ##
//
//        // ## Act ##
//        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
//            cb.query().setMemberStatusCode_Equal_ハンズオン();
//        });
//
//        // ## Assert ##
//        memberList.forEach(member -> {
//            log(member.getMemberName(), member.getMemberStatusCode());
//        });
//    }

//    classificationUndefinedHandlingType=EXCEPTION の動作確認に使用したテストby hase (2026/05/24)
//    public void test_classificationUndefinedHandlingType() throws Exception {
//        // ## Arrange ##
//
//        // ## Act ##
//        ListResultBean<Member> memberList = memberBhv.selectList(cb -> {
//            cb.query().addOrderBy_MemberId_Asc();
//        });
//
//        // ## Assert ##
//        memberList.forEach(member -> {
//            log(member.getMemberName(), member.getMemberStatusCodeAsMemberStatus());
//        });
//    }
}
