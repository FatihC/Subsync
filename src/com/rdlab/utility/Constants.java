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
	public static final String SITE_NAME_TAG = "SITE_NAME";
	public static final String BLOCK_NAME_TAG = "BLOCK_NAME_NUMBER";
	public static final String UAVT_TAG = "UAVT_ADDRESS_NO";
	public static final String INDOOR_TAG = "INDOOR_NUMBER";
	public static final String CHECKED_UAVT = "CHECK_STATUS";
	public static final String LAST_SYNC_TAG = "LAST_SYNC_DATE";
	

	public static final String EDIT_OUTDOOR = "OUTDOOR_NUM_EDIT";
	public static final String EDIT_SITE = "SITE_NAME_EDIT";
	public static final String EDIT_BLOCK = "BLOCK_NAME_EDIT";
	public static final String EDIT_INDOOR = "EDIT_INDOOR";

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

	/*
	 * Fragments Headers
	 */
	public static final String DISTRICT_HEADER_TEXT = "L�TFEN MAHALLE SE��N�Z";
	public static final String VILLAGE_HEADER_TEXT = "L�TFEN K�Y SE��N�Z";
	public static final String STREET_HEADER_TEXT = "L�TFEN MAHALLE/K�Y SE��N�Z";
	public static final String CSBM_HEADER_TEXT = "L�TFEN CADDE/SOKAK/BULVAR/MEYDAN/MEZRA SE��N�Z";
	public static final String BLOCK_HEADER_TEXT = "L�TFEN DI� KAPI NUMARASI SE��N�Z";
	public static final String UNIT_HEADER_TEXT = "L�TFEN �� KAPI NO SE��N�Z";
	public static final String MATCH_TEXT = "E�LE�T�RME";
	public static final String SYNC_HEADER_TEXT = "VER� G�NCELLEME";

	/*
	 * Warnings
	 */
	public static final String USER_NOT_EXIST = "Kullan�c� Ad� ya da �ifre hatas�.";
	public static final String NO_ITEM_SELECTED = "Tesisat No, Saya� No-Marka veya Durum se�ilmelidir.";

	/*
	 * Meter brands
	 */
	public static final ArrayList<String> METER_BRANDS = new ArrayList<String>(
			Arrays.asList("", "ABB", "AEG", "ALFATECH", "AMPY", "ASER",
					"ASSAY ECHELON", "BA�ARI - KAAN", "BAYLAN", "Circulator",
					"EAS M1", "Ekosay", "ELEKTRA", "ELEKTROMED", "EMH ELGAMA",
					"Entes", "ESEM", "FEDERAL", "FLASH", "GANZ", "ISKRA",
					"�SRA", "K�HLER", "K�HLER (AEL)", "L�BRA", "LUNA",
					"M.K.E.", "MAKEL", "MATE�", "MEKS", "Merlin Gerin", "NORA",
					"ONUR", "ORBIS", "Profilo", "PROTON", "SIEMENS",
					"Telefunken", "Vi-KO", "Diger"));

	public static final ArrayList<String> UNIT_STATUS = new ArrayList<String>(
			Arrays.asList("Ortak Kullan�m", "Ev", "�� Yeri", "Resmi Kurum",
					"Ambar", "Bodrum", "Depo", "Hidrofor", "Kazan Dairesi"));

	public static final ArrayList<String> STATUSES = new ArrayList<String>(
			Arrays.asList("", "Birim Yok", "Kapal�", "Saya� Var Ula��lamad�",
					"Saya� yok - Enerjisiz Birim",
					"Saya� Yok - Ortak Kullan�m", "Saya� Yok - S�zme Saya�",
					"Saya� Yok - Abonesiz"));

	public static String SelectedCountyCode = "1115";
	public static String SelectedUniversalCountyCode = "35";
	public static String SelectedCountyName = "Ak�akale";
	public static String SelectedClassName = "com.rdlab.model.Akcakale";
	public static Long LoggedUserSerno;
	public static String LoggedUserName;
	public static String LoggedUserFullname;

}
