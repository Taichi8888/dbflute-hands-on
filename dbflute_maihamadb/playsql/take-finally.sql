-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Member addresses should be only one at any time.
-- - - - - - - - - - -/
select adr.MEMBER_ADDRESS_ID, adr.MEMBER_ID
     , adr.VALID_BEGIN_DATE, adr.VALID_END_DATE
     , adr.ADDRESS
from MEMBER_ADDRESS adr
where exists (select subadr.MEMBER_ADDRESS_ID
              from MEMBER_ADDRESS subadr
              where subadr.MEMBER_ID = adr.MEMBER_ID
                and subadr.VALID_BEGIN_DATE > adr.VALID_BEGIN_DATE
                and subadr.VALID_BEGIN_DATE < adr.VALID_END_DATE
)
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Provisional member should not have formalized datetime.
-- - - - - - - - - - -/
select member.MEMBER_ID, member.MEMBER_NAME, member.MEMBER_STATUS_CODE, member.FORMALIZED_DATETIME
from MEMBER member
where member.MEMBER_STATUS_CODE = 'PRV'
  and member.FORMALIZED_DATETIME is not null
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Member should not be born in the future.
-- - - - - - - - - - -/
select member.MEMBER_ID, member.MEMBER_NAME, member.BIRTHDATE
from MEMBER member
where member.BIRTHDATE > current_date
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Withdrawal member should have withdrawal info.
-- - - - - - - - - - -/
select member.MEMBER_ID, member.MEMBER_NAME
from MEMBER member
  left join MEMBER_WITHDRAWAL wdl on wdl.MEMBER_ID = member.MEMBER_ID
where member.MEMBER_STATUS_CODE = 'WDL'
  and wdl.MEMBER_ID is null
;
