package org.docksidestage.handson.dbflute.bsentity;

import java.util.List;
import java.util.ArrayList;

import org.dbflute.Entity;
import org.dbflute.dbmeta.DBMeta;
import org.dbflute.dbmeta.AbstractEntity;
import org.dbflute.dbmeta.accessory.DomainEntity;
import org.dbflute.optional.OptionalEntity;
import org.docksidestage.handson.dbflute.allcommon.DBMetaInstanceHandler;
import org.docksidestage.handson.dbflute.allcommon.CDef;
import org.docksidestage.handson.dbflute.exentity.*;

/**
 * The entity of (商品カテゴリ)PRODUCT_CATEGORY as TABLE. <br>
 * 商品のカテゴリを表現するマスタ。<br>
 * 自己参照の階層になっている。
 * @author DBFlute(AutoGenerator)
 */
public abstract class BsProductCategory extends AbstractEntity implements DomainEntity {

    // ===================================================================================
    //                                                                          Definition
    //                                                                          ==========
    /** The serial version UID for object serialization. (Default) */
    private static final long serialVersionUID = 1L;

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** (商品カテゴリコード)PRODUCT_CATEGORY_CODE: {PK, NotNull, CHAR(3), classification=ProductCategory} */
    protected String _productCategoryCode;

    /** (商品カテゴリ名称)PRODUCT_CATEGORY_NAME: {NotNull, VARCHAR(50)} */
    protected String _productCategoryName;

    /** (親カテゴリコード)PARENT_CATEGORY_CODE: {IX, CHAR(3), FK to product_category, classification=ProductCategory} */
    protected String _parentCategoryCode;

    // ===================================================================================
    //                                                                             DB Meta
    //                                                                             =======
    /** {@inheritDoc} */
    public DBMeta asDBMeta() {
        return DBMetaInstanceHandler.findDBMeta(asTableDbName());
    }

    /** {@inheritDoc} */
    public String asTableDbName() {
        return "product_category";
    }

    // ===================================================================================
    //                                                                        Key Handling
    //                                                                        ============
    /** {@inheritDoc} */
    public boolean hasPrimaryKeyValue() {
        if (_productCategoryCode == null) { return false; }
        return true;
    }

    // ===================================================================================
    //                                                             Classification Property
    //                                                             =======================
    /**
     * Get the value of productCategoryCode as the classification of ProductCategory. <br>
     * (商品カテゴリコード)PRODUCT_CATEGORY_CODE: {PK, NotNull, CHAR(3), classification=ProductCategory} <br>
     * 商品のカテゴリ。階層構造である
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.ProductCategory getProductCategoryCodeAsProductCategory() {
        return CDef.ProductCategory.of(getProductCategoryCode()).orElse(null);
    }

    /**
     * Set the value of productCategoryCode as the classification of ProductCategory. <br>
     * (商品カテゴリコード)PRODUCT_CATEGORY_CODE: {PK, NotNull, CHAR(3), classification=ProductCategory} <br>
     * 商品のカテゴリ。階層構造である
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setProductCategoryCodeAsProductCategory(CDef.ProductCategory cdef) {
        setProductCategoryCode(cdef != null ? cdef.code() : null);
    }

    /**
     * Get the value of parentCategoryCode as the classification of ProductCategory. <br>
     * (親カテゴリコード)PARENT_CATEGORY_CODE: {IX, CHAR(3), FK to product_category, classification=ProductCategory} <br>
     * 商品のカテゴリ。階層構造である
     * <p>It's treated as case insensitive and if the code value is null, it returns null.</p>
     * @return The instance of classification definition (as ENUM type). (NullAllowed: when the column value is null)
     */
    public CDef.ProductCategory getParentCategoryCodeAsProductCategory() {
        return CDef.ProductCategory.of(getParentCategoryCode()).orElse(null);
    }

    /**
     * Set the value of parentCategoryCode as the classification of ProductCategory. <br>
     * (親カテゴリコード)PARENT_CATEGORY_CODE: {IX, CHAR(3), FK to product_category, classification=ProductCategory} <br>
     * 商品のカテゴリ。階層構造である
     * @param cdef The instance of classification definition (as ENUM type). (NullAllowed: if null, null value is set to the column)
     */
    public void setParentCategoryCodeAsProductCategory(CDef.ProductCategory cdef) {
        setParentCategoryCode(cdef != null ? cdef.code() : null);
    }

    // ===================================================================================
    //                                                              Classification Setting
    //                                                              ======================
    /**
     * Set the value of productCategoryCode as 食品 (FOD). <br>
     * 食品
     */
    public void setProductCategoryCode_食品() {
        setProductCategoryCodeAsProductCategory(CDef.ProductCategory.食品);
    }

    /**
     * Set the value of productCategoryCode as 音楽 (MSC). <br>
     * 音楽
     */
    public void setProductCategoryCode_音楽() {
        setProductCategoryCodeAsProductCategory(CDef.ProductCategory.音楽);
    }

    /**
     * Set the value of productCategoryCode as ハーブ (HEB). <br>
     * ハーブ: 0
     */
    public void setProductCategoryCode_ハーブ() {
        setProductCategoryCodeAsProductCategory(CDef.ProductCategory.ハーブ);
    }

    /**
     * Set the value of productCategoryCode as 楽器 (INS). <br>
     * 楽器: 0
     */
    public void setProductCategoryCode_楽器() {
        setProductCategoryCodeAsProductCategory(CDef.ProductCategory.楽器);
    }

    /**
     * Set the value of productCategoryCode as 音楽cd (MCD). <br>
     * 音楽CD: 0
     */
    public void setProductCategoryCode_音楽cd() {
        setProductCategoryCodeAsProductCategory(CDef.ProductCategory.音楽cd);
    }

    /**
     * Set the value of parentCategoryCode as 食品 (FOD). <br>
     * 食品
     */
    public void setParentCategoryCode_食品() {
        setParentCategoryCodeAsProductCategory(CDef.ProductCategory.食品);
    }

    /**
     * Set the value of parentCategoryCode as 音楽 (MSC). <br>
     * 音楽
     */
    public void setParentCategoryCode_音楽() {
        setParentCategoryCodeAsProductCategory(CDef.ProductCategory.音楽);
    }

    /**
     * Set the value of parentCategoryCode as ハーブ (HEB). <br>
     * ハーブ: 0
     */
    public void setParentCategoryCode_ハーブ() {
        setParentCategoryCodeAsProductCategory(CDef.ProductCategory.ハーブ);
    }

    /**
     * Set the value of parentCategoryCode as 楽器 (INS). <br>
     * 楽器: 0
     */
    public void setParentCategoryCode_楽器() {
        setParentCategoryCodeAsProductCategory(CDef.ProductCategory.楽器);
    }

    /**
     * Set the value of parentCategoryCode as 音楽cd (MCD). <br>
     * 音楽CD: 0
     */
    public void setParentCategoryCode_音楽cd() {
        setParentCategoryCodeAsProductCategory(CDef.ProductCategory.音楽cd);
    }

    // ===================================================================================
    //                                                        Classification Determination
    //                                                        ============================
    /**
     * Is the value of productCategoryCode 食品? <br>
     * 食品
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isProductCategoryCode食品() {
        CDef.ProductCategory cdef = getProductCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.食品) : false;
    }

    /**
     * Is the value of productCategoryCode 音楽? <br>
     * 音楽
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isProductCategoryCode音楽() {
        CDef.ProductCategory cdef = getProductCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.音楽) : false;
    }

    /**
     * Is the value of productCategoryCode ハーブ? <br>
     * ハーブ: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isProductCategoryCodeハーブ() {
        CDef.ProductCategory cdef = getProductCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.ハーブ) : false;
    }

    /**
     * Is the value of productCategoryCode 楽器? <br>
     * 楽器: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isProductCategoryCode楽器() {
        CDef.ProductCategory cdef = getProductCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.楽器) : false;
    }

    /**
     * Is the value of productCategoryCode 音楽cd? <br>
     * 音楽CD: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isProductCategoryCode音楽cd() {
        CDef.ProductCategory cdef = getProductCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.音楽cd) : false;
    }

    /**
     * Is the value of parentCategoryCode 食品? <br>
     * 食品
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isParentCategoryCode食品() {
        CDef.ProductCategory cdef = getParentCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.食品) : false;
    }

    /**
     * Is the value of parentCategoryCode 音楽? <br>
     * 音楽
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isParentCategoryCode音楽() {
        CDef.ProductCategory cdef = getParentCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.音楽) : false;
    }

    /**
     * Is the value of parentCategoryCode ハーブ? <br>
     * ハーブ: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isParentCategoryCodeハーブ() {
        CDef.ProductCategory cdef = getParentCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.ハーブ) : false;
    }

    /**
     * Is the value of parentCategoryCode 楽器? <br>
     * 楽器: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isParentCategoryCode楽器() {
        CDef.ProductCategory cdef = getParentCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.楽器) : false;
    }

    /**
     * Is the value of parentCategoryCode 音楽cd? <br>
     * 音楽CD: 0
     * <p>It's treated as case insensitive and if the code value is null, it returns false.</p>
     * @return The determination, true or false.
     */
    public boolean isParentCategoryCode音楽cd() {
        CDef.ProductCategory cdef = getParentCategoryCodeAsProductCategory();
        return cdef != null ? cdef.equals(CDef.ProductCategory.音楽cd) : false;
    }

    // ===================================================================================
    //                                                                    Foreign Property
    //                                                                    ================
    /** (商品カテゴリ)PRODUCT_CATEGORY by my PARENT_CATEGORY_CODE, named 'productCategorySelf'. */
    protected OptionalEntity<ProductCategory> _productCategorySelf;

    /**
     * [get] (商品カテゴリ)PRODUCT_CATEGORY by my PARENT_CATEGORY_CODE, named 'productCategorySelf'. <br>
     * Optional: alwaysPresent(), ifPresent().orElse(), get(), ...
     * @return The entity of foreign property 'productCategorySelf'. (NotNull, EmptyAllowed: when e.g. null FK column, no setupSelect)
     */
    public OptionalEntity<ProductCategory> getProductCategorySelf() {
        if (_productCategorySelf == null) { _productCategorySelf = OptionalEntity.relationEmpty(this, "productCategorySelf"); }
        return _productCategorySelf;
    }

    /**
     * [set] (商品カテゴリ)PRODUCT_CATEGORY by my PARENT_CATEGORY_CODE, named 'productCategorySelf'.
     * @param productCategorySelf The entity of foreign property 'productCategorySelf'. (NullAllowed)
     */
    public void setProductCategorySelf(OptionalEntity<ProductCategory> productCategorySelf) {
        _productCategorySelf = productCategorySelf;
    }

    // ===================================================================================
    //                                                                   Referrer Property
    //                                                                   =================
    /** (商品)PRODUCT by PRODUCT_CATEGORY_CODE, named 'productList'. */
    protected List<Product> _productList;

    /**
     * [get] (商品)PRODUCT by PRODUCT_CATEGORY_CODE, named 'productList'.
     * @return The entity list of referrer property 'productList'. (NotNull: even if no loading, returns empty list)
     */
    public List<Product> getProductList() {
        if (_productList == null) { _productList = newReferrerList(); }
        return _productList;
    }

    /**
     * [set] (商品)PRODUCT by PRODUCT_CATEGORY_CODE, named 'productList'.
     * @param productList The entity list of referrer property 'productList'. (NullAllowed)
     */
    public void setProductList(List<Product> productList) {
        _productList = productList;
    }

    /** (商品カテゴリ)PRODUCT_CATEGORY by PARENT_CATEGORY_CODE, named 'productCategorySelfList'. */
    protected List<ProductCategory> _productCategorySelfList;

    /**
     * [get] (商品カテゴリ)PRODUCT_CATEGORY by PARENT_CATEGORY_CODE, named 'productCategorySelfList'.
     * @return The entity list of referrer property 'productCategorySelfList'. (NotNull: even if no loading, returns empty list)
     */
    public List<ProductCategory> getProductCategorySelfList() {
        if (_productCategorySelfList == null) { _productCategorySelfList = newReferrerList(); }
        return _productCategorySelfList;
    }

    /**
     * [set] (商品カテゴリ)PRODUCT_CATEGORY by PARENT_CATEGORY_CODE, named 'productCategorySelfList'.
     * @param productCategorySelfList The entity list of referrer property 'productCategorySelfList'. (NullAllowed)
     */
    public void setProductCategorySelfList(List<ProductCategory> productCategorySelfList) {
        _productCategorySelfList = productCategorySelfList;
    }

    protected <ELEMENT> List<ELEMENT> newReferrerList() { // overriding to import
        return new ArrayList<ELEMENT>();
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    @Override
    protected boolean doEquals(Object obj) {
        if (obj instanceof BsProductCategory) {
            BsProductCategory other = (BsProductCategory)obj;
            if (!xSV(_productCategoryCode, other._productCategoryCode)) { return false; }
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected int doHashCode(int initial) {
        int hs = initial;
        hs = xCH(hs, asTableDbName());
        hs = xCH(hs, _productCategoryCode);
        return hs;
    }

    @Override
    protected String doBuildStringWithRelation(String li) {
        StringBuilder sb = new StringBuilder();
        if (_productCategorySelf != null && _productCategorySelf.isPresent())
        { sb.append(li).append(xbRDS(_productCategorySelf, "productCategorySelf")); }
        if (_productList != null) { for (Product et : _productList)
        { if (et != null) { sb.append(li).append(xbRDS(et, "productList")); } } }
        if (_productCategorySelfList != null) { for (ProductCategory et : _productCategorySelfList)
        { if (et != null) { sb.append(li).append(xbRDS(et, "productCategorySelfList")); } } }
        return sb.toString();
    }
    protected <ET extends Entity> String xbRDS(org.dbflute.optional.OptionalEntity<ET> et, String name) { // buildRelationDisplayString()
        return et.get().buildDisplayString(name, true, true);
    }

    @Override
    protected String doBuildColumnString(String dm) {
        StringBuilder sb = new StringBuilder();
        sb.append(dm).append(xfND(_productCategoryCode));
        sb.append(dm).append(xfND(_productCategoryName));
        sb.append(dm).append(xfND(_parentCategoryCode));
        if (sb.length() > dm.length()) {
            sb.delete(0, dm.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    @Override
    protected String doBuildRelationString(String dm) {
        StringBuilder sb = new StringBuilder();
        if (_productCategorySelf != null && _productCategorySelf.isPresent())
        { sb.append(dm).append("productCategorySelf"); }
        if (_productList != null && !_productList.isEmpty())
        { sb.append(dm).append("productList"); }
        if (_productCategorySelfList != null && !_productCategorySelfList.isEmpty())
        { sb.append(dm).append("productCategorySelfList"); }
        if (sb.length() > dm.length()) {
            sb.delete(0, dm.length()).insert(0, "(").append(")");
        }
        return sb.toString();
    }

    @Override
    public ProductCategory clone() {
        return (ProductCategory)super.clone();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] (商品カテゴリコード)PRODUCT_CATEGORY_CODE: {PK, NotNull, CHAR(3), classification=ProductCategory} <br>
     * @return The value of the column 'PRODUCT_CATEGORY_CODE'. (basically NotNull if selected: for the constraint)
     */
    public String getProductCategoryCode() {
        checkSpecifiedProperty("productCategoryCode");
        return convertEmptyToNull(_productCategoryCode);
    }

    /**
     * [set] (商品カテゴリコード)PRODUCT_CATEGORY_CODE: {PK, NotNull, CHAR(3), classification=ProductCategory} <br>
     * @param productCategoryCode The value of the column 'PRODUCT_CATEGORY_CODE'. (basically NotNull if update: for the constraint)
     */
    protected void setProductCategoryCode(String productCategoryCode) {
        checkClassificationCode("PRODUCT_CATEGORY_CODE", CDef.DefMeta.ProductCategory, productCategoryCode);
        registerModifiedProperty("productCategoryCode");
        _productCategoryCode = productCategoryCode;
    }

    /**
     * [get] (商品カテゴリ名称)PRODUCT_CATEGORY_NAME: {NotNull, VARCHAR(50)} <br>
     * @return The value of the column 'PRODUCT_CATEGORY_NAME'. (basically NotNull if selected: for the constraint)
     */
    public String getProductCategoryName() {
        checkSpecifiedProperty("productCategoryName");
        return convertEmptyToNull(_productCategoryName);
    }

    /**
     * [set] (商品カテゴリ名称)PRODUCT_CATEGORY_NAME: {NotNull, VARCHAR(50)} <br>
     * @param productCategoryName The value of the column 'PRODUCT_CATEGORY_NAME'. (basically NotNull if update: for the constraint)
     */
    public void setProductCategoryName(String productCategoryName) {
        registerModifiedProperty("productCategoryName");
        _productCategoryName = productCategoryName;
    }

    /**
     * [get] (親カテゴリコード)PARENT_CATEGORY_CODE: {IX, CHAR(3), FK to product_category, classification=ProductCategory} <br>
     * 最上位の場合はデータなし。
     * @return The value of the column 'PARENT_CATEGORY_CODE'. (NullAllowed even if selected: for no constraint)
     */
    public String getParentCategoryCode() {
        checkSpecifiedProperty("parentCategoryCode");
        return convertEmptyToNull(_parentCategoryCode);
    }

    /**
     * [set] (親カテゴリコード)PARENT_CATEGORY_CODE: {IX, CHAR(3), FK to product_category, classification=ProductCategory} <br>
     * 最上位の場合はデータなし。
     * @param parentCategoryCode The value of the column 'PARENT_CATEGORY_CODE'. (NullAllowed: null update allowed for no constraint)
     */
    protected void setParentCategoryCode(String parentCategoryCode) {
        checkClassificationCode("PARENT_CATEGORY_CODE", CDef.DefMeta.ProductCategory, parentCategoryCode);
        registerModifiedProperty("parentCategoryCode");
        _parentCategoryCode = parentCategoryCode;
    }

    /**
     * For framework so basically DON'T use this method.
     * @param productCategoryCode The value of the column 'PRODUCT_CATEGORY_CODE'. (basically NotNull if update: for the constraint)
     */
    public void mynativeMappingProductCategoryCode(String productCategoryCode) {
        setProductCategoryCode(productCategoryCode);
    }

    /**
     * For framework so basically DON'T use this method.
     * @param parentCategoryCode The value of the column 'PARENT_CATEGORY_CODE'. (NullAllowed: null update allowed for no constraint)
     */
    public void mynativeMappingParentCategoryCode(String parentCategoryCode) {
        setParentCategoryCode(parentCategoryCode);
    }
}
