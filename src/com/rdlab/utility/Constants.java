package com.rdlab.utility;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {
	public static final String CITY_CODE = "63";
	public static final String COUNTY_CODE_TAG = "COUNTY_CODE";
	public static final String DISTRICT_CODE_TAG = "DISTRICT_CODE";
	public static final String VILLAGE_CODE_TAG = "VILLAGE_CODE";
	public static final String STREET_CODE_TAG = "STREET_CODE";
	public static final String CSBM_CODE_TAG = "CSBM_CODE";
	public static final String DOOR_NUMBER_TAG = "DOOR_NUMBER";
	public static final String UAVT_TAG = "UAVT_ADDRESS_NO";
	public static final String INDOOR_TAG = "INDOOR_NUMBER";

	public static final String DISTRICT_NAME_TAG = "DISTRICT_NAME";
	public static final String VILLAGE_NAME_TAG = "VILLAGE_NAME";
	public static final String STREET_NAME_TAG = "STREET_NAME";
	public static final String CSBM_NAME_TAG = "CSBM_NAME";

	public static final String HOME_LIST_STATE = "HOME_LIST_STATE";
	public static final String DISTRICT_LIST_STATE = "DISTRICT_LIST_STATE";
	public static final String VILLAGE_LIST_STATE = "VILLAGE_LIST_STATE";
	public static final String STREET_LIST_STATE = "STREET_LIST_STATE";
	public static final String CSBM_LIST_STATE = "CSBM_LIST_STATE";

	public static final String HALILIYE = "1";
	public static final String EYYUBIYE = "2";
	public static final String VIRANSEHIR = "3";
	public static final String SIVEREK = "4";
	public static final String KARAKOPRU = "5";
	public static final String BIRECIK = "6";
	public static final String SURUC = "7";
	public static final String AKCAKALE = "8";
	public static final String BOZOVA = "9";
	public static final String CEYLANPINAR = "10";
	public static final String HALFETI = "11";
	public static final String HARRAN = "12";
	public static final String HILVAN = "13";

	public static final String DISTRICT_HEADER_TEXT = "LÜTFEN MAHALLE SEÇÝNÝZ";
	public static final String VILLAGE_HEADER_TEXT = "LÜTFEN KÖY SEÇÝNÝZ";
	public static final String STREET_HEADER_TEXT = "LÜTFEN CADDE SEÇÝNÝZ";
	public static final String CSBM_HEADER_TEXT = "LÜTFEN SOKAK/BULVAR/MEYDAN/MEZRA SEÇÝNÝZ";
	public static final String BLOCK_HEADER_TEXT = "LÜTFEN DIÞ KAPI NUMARASI SEÇÝNÝZ";

	/*
	 * Warnings
	 */
	public static final String USER_NOT_EXIST = "Kullanýcý Adý ya da þifre hatasý.";

	/*
	 * Meter brands
	 */
	public static final ArrayList<String> METER_BRANDS = new ArrayList<String>(
			Arrays.asList("ABB", "AEG", "ALFATECH", "AMPY", "ASER",
					"ASSAY ECHELON", "BAÞARI - KAAN", "BAYLAN", "Circulator",
					"EAS M1", "Ekosay", "ELEKTRA", "ELEKTROMED", "EMH ELGAMA",
					"Entes", "ESEM", "FEDERAL", "FLASH", "GANZ", "ISKRA",
					"ÝSRA", "KÖHLER", "KÖHLER (AEL)", "LÝBRA", "LUNA",
					"M.K.E.", "MAKEL", "MATEÞ", "MEKS", "Merlin Gerin", "NORA",
					"ONUR", "ORBIS", "Profilo", "PROTON", "SIEMENS",
					"Telefunken", "Vi-KO", "Diger"));

	public static final ArrayList<String> STATUSES = new ArrayList<String>(
			Arrays.asList("Birim Yok", "Kapalý", "Sayaç Var Ulaþýlamadý",
					"Sayaç yok - Enerjisiz Birim",
					"Sayaç Yok - Ortak Kullaným", "Sayaç Yok - Süzme Sayaç",
					"Sayaç Yok - Abonesiz"));

	public static String SelectedCountyCode = "1115";
	public static String SelectedUniversalCountyCode = "35";
	public static String SelectedCountyName = "Akçakale";
	public static String SelectedClassName = "com.rdlab.model.Akcakale";
	public static Long LoggedUserSerno;
	public static String LoggedUserName;
	public static String LoggedUserFullname;

}
