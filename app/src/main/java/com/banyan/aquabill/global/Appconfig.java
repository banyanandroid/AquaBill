package com.banyan.aquabill.global;

public class Appconfig {

    static String base_url = "http://epictech.in/aqua_bill/Apicontroller/";

    public static String url_login = base_url + "dealerlogin";

    public static String url_profile = base_url + "dealerprofile";
    public static String url_state_list = base_url + "statelist";
    public static String url_city_list = base_url + "citylist";
    public static String url_update_profile = base_url + "dealerprofile_update";

    public static String url_dealerpdu = base_url + "dealerpdu";
    public static String url_pdudetails = base_url + "pdudetails";

    public static String url_brand_list = base_url + "brandlist";
    public static String url_product_details = base_url + "pricedetails";
    public static String url_test_report = base_url + "labreport";

    public static String  url_raw_material_list = base_url + "rawmaterial_list";
    public static String  url_edit_purchase = base_url + "edit_purchase";
    public static String  url_update_purchase = base_url + "update_purchase";

    public static String url_supplier_list = base_url + "supplier_list";
    public static String url_add_supplier = base_url + "addsupplier";
    public static String url_supplier_details = base_url + "supplier_details";
    public static String url_update_supplier__profile = base_url + "updatesupplier";

    public static String url_add_purchase = base_url + "purchase_rawmaterial";
    public static String url_list_purchase = base_url + "purchaselist";
    public static String url_add_opening_stock = base_url + "addopening_stock";
    public static String url_raw_mat_live_stock = base_url + "rawmaterial_stock";
    public static String url_add_demaged_stock = base_url + "damage_rawmaterial";
    public static String url_list_purchase_report = base_url + "purchase_report";

    public static String url_check_customer = base_url + "checkcustomer";
    public static String url_add_customer = base_url + "addcustomer";
    public static String url_list_customer = base_url + "listcustomer";
    public static String url_view_customer = base_url + "view_customer";

    public static String url_customer_pricedetails = base_url + "customer_pricedetails";
    public static String url_customer_selling_price = base_url + "product_price";

    public static String url_invoice_list = base_url + "invoice_list";
    public static String url_getinvoiceno = base_url + "getinvoiceno";
    public static String url_invoice_brandlist = base_url + "invoice_brandlist";
    public static String url_invoice_productlist = base_url + "invoice_productlist";
    public static String url_add_invoice = base_url + "invoice_insert";

    public static String url_raw_mat_invoice_list = base_url + "invoice_rawlist";
    public static String url_invoice_raw_mat_list = base_url + "rawmaterial_lists";
    public static String url_add_raw_mat_invoice = base_url + "invoiceraw_insert";

    public static String url_receipt_list = base_url + "receipt_list";
    public static String url_getreceiptno = base_url + "getreceiptno";
    public static String url_add_received_cash = base_url + "addreceipt";

    public static String url_receive_can_list = base_url + "listemptycan";
    public static String url_getreceiptno_receive = base_url + "getempty_receiptno";
    public static String url_add_receive_can = base_url + "addemptycan";

    public static String url_dashboard = base_url + "live_records";
    public static String url_dashboard_dealer_pendingpdu = base_url + "dealer_pendingpdu";

    public static String url_sales_report = base_url + "sales_report";
    public static String url_receipt_report = base_url + "receipt_report";
    public static String url_list_can = base_url + "list_can";
    public static String url_emptycan_reports = base_url + "emptycan_reports";
    public static String url_customeroutstanding_report = base_url + "customeroutstanding_report";
    public static String url_customertrans_reports = base_url + "customertrans_reports";
    public static String url_customer_emptycan = base_url + "customer_emptycan";
}
