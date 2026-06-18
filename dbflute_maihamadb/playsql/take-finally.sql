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
                  and subadr.VALID_BEGIN_DATE <= adr.VALID_END_DATE
               )
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Provisional member should not have formalized datetime.
-- - - - - - - - - - -/
select mb.MEMBER_ID, mb.MEMBER_NAME, mb.MEMBER_STATUS_CODE, mb.FORMALIZED_DATETIME
  from MEMBER mb
 where mb.MEMBER_STATUS_CODE = 'PRV'
   and mb.FORMALIZED_DATETIME is not null
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Member should not be born in the future.
-- - - - - - - - - - -/
select mb.MEMBER_ID, mb.MEMBER_NAME, mb.BIRTHDATE
  from MEMBER mb
 where mb.BIRTHDATE > current_date
;

-- #df:assertListZero#
-- /- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
-- Withdrawal member should have withdrawal info.
-- - - - - - - - - - -/
select mb.MEMBER_ID, mb.MEMBER_NAME
  from MEMBER mb
    left join MEMBER_WITHDRAWAL wdl on wdl.MEMBER_ID = mb.MEMBER_ID
 where mb.MEMBER_STATUS_CODE = 'WDL'
   and wdl.MEMBER_ID is null
;
