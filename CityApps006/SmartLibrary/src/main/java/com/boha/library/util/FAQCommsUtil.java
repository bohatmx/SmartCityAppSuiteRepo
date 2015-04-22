package com.boha.library.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;



public class FAQCommsUtil {

	public interface CommsListener {
		void onSuccess(String response) throws CommsException;
		void onError(String message);
	}
	public interface FAQListener {
		void onSuccess(FaqStrings faqStrings) throws CommsException;
		void onError(String message);
	}

	public static final String

			ACCOUNTS = "AccountsPayments.html",
			BUILDING_PLANS = "BuildingPlans.html",
			CLEANSING_SOLIDWASTE = "CleansingSolidWaste.html",
			HEALTH = "Health.html",
			ELECTRICITY = "Electricity.html",
			METRO_POLICE = "MetroPolice.html",
			RATES_TAXES = "RatesTaxes.html",
			SOCIAL_SERVICES = "SocialServices.html",
			WATER_SANITATION = "WaterSanitation.html";

	static String request, response;
	static CommsListener commsListener;
	static FAQListener faqListener;
	static Context ctx;
	public static void getFAQfiles(Context context, Integer municipalityID, FAQListener listener) throws CommsException {
		faqListener = listener;
		ctx = context;
		final FaqStrings faqStrings = new FaqStrings();
		final StringBuilder xx = new StringBuilder();
		xx.append(Statics.IMAGE_URL).append("smartcity_images/municipality");
		xx.append(municipalityID.intValue()).append("/faq/");

		getSimpleData(xx.toString() + ACCOUNTS, new CommsListener() {
			@Override
			public void onSuccess(String response) throws CommsException {
				faqStrings.setAccountsFAQ(response);
				getSimpleData(xx.toString() + BUILDING_PLANS, new CommsListener() {
					@Override
					public void onSuccess(String response) throws CommsException {
						faqStrings.setBuildingPlansFAQ(response);
						getSimpleData(xx.toString() + CLEANSING_SOLIDWASTE, new CommsListener() {
							@Override
							public void onSuccess(String response) throws CommsException {
								faqStrings.setCleaningWasteFAQ(response);
								getSimpleData(xx.toString() + ELECTRICITY, new CommsListener() {
									@Override
									public void onSuccess(String response) throws CommsException {
										faqStrings.setElectricityFAQ(response);
										getSimpleData(xx.toString() + HEALTH, new CommsListener() {
											@Override
											public void onSuccess(String response) throws CommsException {
												faqStrings.setHealthFAQ(response);
												getSimpleData(xx.toString() + METRO_POLICE, new CommsListener() {
													@Override
													public void onSuccess(String response) throws CommsException {
														faqStrings.setMetroPoliceFAQ(response);
														getSimpleData(xx.toString() + RATES_TAXES, new CommsListener() {
															@Override
															public void onSuccess(String response) throws CommsException {
																faqStrings.setRatesTaxesFAQ(response);
																getSimpleData(xx.toString() + SOCIAL_SERVICES, new CommsListener() {
																	@Override
																	public void onSuccess(String response) throws CommsException {
																		faqStrings.setSocialServicesFAQ(response);
																		getSimpleData(xx.toString() + WATER_SANITATION, new CommsListener() {
																			@Override
																			public void onSuccess(String response) throws CommsException {
																				faqStrings.setWaterSanitationFAQ(response);
																				CacheUtil.cacheFAQ(ctx, faqStrings, new CacheUtil.CacheListener() {
																					@Override
																					public void onDataCached() throws CommsException {
																						faqListener.onSuccess(faqStrings);
																					}

																					@Override
																					public void onError() {

																					}
																				});

																			}

																			@Override
																			public void onError(String message) {

																			}
																		});
																	}

																	@Override
																	public void onError(String message) {

																	}
																});
															}

															@Override
															public void onError(String message) {

															}
														});
													}

													@Override
													public void onError(String message) {

													}
												});
											}

											@Override
											public void onError(String message) {

											}
										});
									}

									@Override
									public void onError(String message) {

									}
								});
							}

							@Override
							public void onError(String message) {

							}
						});
					}

					@Override
					public void onError(String message) {

					}
				});
			}

			@Override
			public void onError(String message) {

			}
		});
	}
	public static void getSimpleData(String url, CommsListener listener)
			throws CommsException {
		request = url;
		commsListener = listener;
		new CTask().execute();

	}
	static class CTask extends AsyncTask<Void,Void,Integer> {

		@Override
		protected Integer doInBackground(Void... params) {
			Log.d(COMMS, "doInBackground: sending request: .......\n" + request);
			HttpURLConnection con = null;
			URL url;
			InputStream is = null;

			try {
				url = new URL(request);
				con = (HttpURLConnection) url.openConnection();
				con.setRequestMethod("GET");
				con.connect();
				is = con.getInputStream();
				int httpCode = con.getResponseCode();
				String msg = con.getResponseMessage();
				Log.d(COMMS, "### HTTP response code: " + httpCode + " msg: " + msg);
				response = readStream(is);
				Log.d(COMMS, "### RESPONSE: \n" + response);

			} catch (IOException e) {
				Log.e(COMMS, "Houston, we have an IOException. F%$%K!", e);
				return 9;
			} catch (Exception e) {
				return 6;
			} finally {
				try {
					is.close();
				} catch (Exception e) {
				}
			}
			return 0;
		}
		@Override
		protected void onPostExecute(Integer res) {
			if (res == 0) {
				try {
					commsListener.onSuccess(response);
				} catch (CommsException e) {
					e.printStackTrace();
				}
			} else {
				commsListener.onError("Unable to process your request");
			}
		}
	}



	private static String readStream(InputStream in) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader r = new BufferedReader(new InputStreamReader(in), 1024);
		for (String line = r.readLine(); line != null; line = r.readLine()) {
			sb.append(line);
		}
		in.close();
		return sb.toString();
	}

	private static final String COMMS = FAQCommsUtil.class.getSimpleName();
}
