package com.infinity.weather;

import android.database.MatrixCursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.db.chart.Tools;
import com.db.chart.model.LineSet;
import com.db.chart.view.AxisController;
import com.db.chart.view.LineChartView;
import com.db.chart.view.animation.Animation;
import com.db.chart.view.animation.easing.ExpoEase;
import com.google.gson.Gson;
import com.infinity.weather.model.db.Element;
import com.infinity.weather.model.weather.Channel;
import com.infinity.weather.model.weather.Forecast;
import com.infinity.weather.model.weather.Weather;
import com.infinity.weather.network.WeatherRequest;
import com.infinity.weather.utils.DataBaseHelper;
import com.infinity.weather.widget.ExtendTextView;
import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends RoboSpiceActivity {

    public static final String WEATHER_OBJECT = "weather_object";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Toolbar toolbar;
    private Drawer drawerResult = null;
    private AccountHeader headerResult;

    private static final int EDIT_LOCATIONS_ID = 0;
    private static final int CURRENT_LOCATION_ID = 1;
    private static final int SETTINGS_ID = 2;
    private static final int SHARE_ID = 3;
    private static final int HELP_ID = 4;
    private static final int CONTACT_ID = 5;

    private static final int POSITION_INSERT = 2;

    private static final String DB_NAME = "city";
    private static final int DB_VERSION = 1;

    @BindView(R.id.linechart) LineChartView mLineChartView;
    @BindView(R.id.city_region) ExtendTextView mCityRegion;
    @BindView(R.id.temp) ExtendTextView mTemp;
    @BindView(R.id.description) ExtendTextView mDescription;
    @BindView(R.id.humidity) ExtendTextView mHumidity;
    @BindView(R.id.wind) ExtendTextView mWind;
    @BindView(R.id.pressure) ExtendTextView mPressure;

    @BindView(R.id.weather_icon_one) ImageView mIconOne;
    @BindView(R.id.weather_icon_two) ImageView mIconTwo;
    @BindView(R.id.weather_icon_three) ImageView mIconThree;
    @BindView(R.id.weather_icon_four) ImageView mIconFour;
    @BindView(R.id.weather_icon_five) ImageView mIconFive;
    @BindView(R.id.weather_high_one) TextView mHighOne;
    @BindView(R.id.weather_high_two) TextView mHighTwo;
    @BindView(R.id.weather_high_three) TextView mHighThree;
    @BindView(R.id.weather_high_four) TextView mHighFour;
    @BindView(R.id.weather_high_five) TextView mHighFive;
    @BindView(R.id.weather_low_one) TextView mLowOne;
    @BindView(R.id.weather_low_two) TextView mLowTwo;
    @BindView(R.id.weather_low_three) TextView mLowThree;
    @BindView(R.id.weather_low_four) TextView mLowFour;
    @BindView(R.id.weather_low_five) TextView mLowFive;
    @BindView(R.id.weather_day_one) TextView mDayOne;
    @BindView(R.id.weather_day_two) TextView mDayTwo;
    @BindView(R.id.weather_day_three) TextView mDayThree;
    @BindView(R.id.weather_day_four) TextView mDayFour;
    @BindView(R.id.weather_day_five) TextView mDayFive;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Init ToolBar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNavigationDrawer();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Weather result = new Gson().fromJson(extras.getString(WEATHER_OBJECT), Weather.class);
            inflateScreen(result.getQuery().getResults().getChannel());
        }
    }

    private void clearScreen(){
        mCityRegion.setText("");
        mTemp.setText("--");
        mDescription.setText("--");
        mHumidity.setText("--%");
        mWind.setText("--");
        mPressure.setText("--");
    }

    private void inflateScreen(Channel weather){
        if (weather.getLocation().getRegion().isEmpty()){
            mCityRegion.setText(weather.getLocation().getCity());
        }else{
            mCityRegion.setText(weather.getLocation().getCity()+", "+weather.getLocation().getRegion());
        }
        mTemp.setText(weather.getItem().getCondition().getTemp());
        mDescription.setText(weather.getItem().getCondition().getText());
        mHumidity.setText(weather.getAtmosphere().getHumidity()+"%");
        mWind.setText(weather.getWind().getSpeed()+weather.getUnits().getSpeed());
        mPressure.setText(weather.getAtmosphere().getPressure()+weather.getUnits().getPressure());

        createChartView(weather.getItem().getForecast());
        inflateTable(weather.getItem().getForecast());
    }

    private void inflateTable(List<Forecast> forecasts){
        int i = 1;
        for (Forecast item : forecasts){
            switch (i){
                case 1:{
                    mIconOne.setImageResource(getIcon(item.getCode()));
                    mHighOne.setText(item.getHigh());
                    mLowOne.setText(item.getLow());
                    mDayOne.setText(item.getDay());
                    break;
                }
                case 2:{
                    mIconTwo.setImageResource(getIcon(item.getCode()));
                    mHighTwo.setText(item.getHigh());
                    mLowTwo.setText(item.getLow());
                    mDayTwo.setText(item.getDay());
                    break;
                }
                case 3:{
                    mIconThree.setImageResource(getIcon(item.getCode()));
                    mHighThree.setText(item.getHigh());
                    mLowThree.setText(item.getLow());
                    mDayThree.setText(item.getDay());
                    break;
                }
                case 4:{
                    mIconFour.setImageResource(getIcon(item.getCode()));
                    mHighFour.setText(item.getHigh());
                    mLowFour.setText(item.getLow());
                    mDayFour.setText(item.getDay());
                    break;
                }
                case 5:{
                    mIconFive.setImageResource(getIcon(item.getCode()));
                    mHighFive.setText(item.getHigh());
                    mLowFive.setText(item.getLow());
                    mDayFive.setText(item.getDay());
                    break;
                }
            }
            i++;
        }
    }

    private int getIcon(String value){
        int code = Integer.parseInt(value);
        switch (code){
            case 0:{
                return R.drawable.hourly_tornado_day_night;
            }
            case 1: case 2:{
                return R.drawable.hourly_hurricane_day_night;
            }
            case 3: case 4:{
                return R.drawable.hourly_thundershowers_day_night;
            }
            case 5:{
                return R.drawable.hourly_snow_rain_mix_day_night;
            }
            case 6: case 7:{
                return R.drawable.hourly_sleet_mix_day_night;
            }
            case 8: case 9: case 10:{
                return R.drawable.hourly_freezing_rain_day_night;
            }
            case 11: case 12:{
                return R.drawable.hourly_scattered_showers_day_night;
            }
            case 13: case 14: case 15: case 16:{
                return R.drawable.hourly_snow_day_night;
            }
            case 17:{
                return R.drawable.hourly_hail_day_night;
            }
            case 18:{
                return R.drawable.hourly_sleet_day_night;
            }
            case 19:{
                return R.drawable.hourly_dust_day_night;
            }
            case 20:{
                return R.drawable.hourly_fog_day_night;
            }
            case 21:{
                return R.drawable.hourly_haze_day_night;
            }
            case 22:{
                return R.drawable.hourly_smoky_day_night;
            }
            case 23: case 24:{
                return R.drawable.hourly_windy_day_night;
            }
            case 25: {
                return R.drawable.hourly_flurries_day_night;
            }
            case 26:{
                return R.drawable.hourly_cloudy_day_night;
            }
            case 27: case 28:{
                return R.drawable.hourly_mostly_cloudy_day_night;
            }
            case 29: {
                return R.drawable.hourly_partly_cloudy_night;
            }
            case 30: {
                return R.drawable.hourly_partly_cloudy_day;
            }
            case 31:{
                return R.drawable.hourly_clear_night;
            }
            case 32: case 36:{
                return R.drawable.hourly_clear_day;
            }
            case 33:{
                return R.drawable.hourly_fair_night;
            }
            case 34:{
                return R.drawable.hourly_fair_day;
            }
            case 35:{
                return R.drawable.hourly_hail_day_night;
            }
            case 37: case 38: case 39: case 47:{
                return R.drawable.hourly_scatttered_thundershowers_day;
            }
            case 40:{
                return R.drawable.hourly_scattered_showers_day_night;
            }
            case 41: case 42: case 43: case 46:{
                return R.drawable.hourly_heavy_snow_day_night;
            }
            case 44:{
                return R.drawable.hourly_cloudy_day_night;
            }
            case 45:{
                return R.drawable.hourly_thundershowers_day_night;
            }
            case 3200: default:{
                return R.drawable.na;
            }
        }
    }

    @Override
    public void onBackPressed(){
        if(drawerResult.isDrawerOpen()){
            drawerResult.closeDrawer();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_search) {
//            return true;
//        }
        return super.onOptionsItemSelected(item);
    }

    private void createChartView(List<Forecast> forecasts){
        ArrayList<Integer> values = new ArrayList<>();
        for(Forecast item : forecasts)
            values.add(Integer.parseInt(item.getHigh()));
        LineSet lineSet = new LineSet();
        int max = -2000;
        int min = 2000;
        for (int val : values) {
            if (val > max)
                max = val;
            if (val < min)
                min = val;
            lineSet.addPoint("", val);
        }
        lineSet.setSmooth(true);
        lineSet.setColor(getResources().getColor(R.color.md_white_1000));
        lineSet.setFill(getResources().getColor(R.color.test_color));
        lineSet.setThickness(4);
        mLineChartView.reset();
        mLineChartView.addData(lineSet);
        mLineChartView.setTopSpacing(Tools.fromDpToPx(15))
                .setBorderSpacing(Tools.fromDpToPx(0))
                .setAxisBorderValues(min-1, max+1, 1)
                .setXLabels(AxisController.LabelPosition.NONE)
                .setYLabels(AxisController.LabelPosition.NONE)
                .setXAxis(false)
                .setYAxis(false);

        Animation anim = new Animation(5000)
                .setEasing(new ExpoEase())
                .setStartPoint(-1, 0);
        mLineChartView.show(anim);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createNavigationDrawer(){
        //Account switcher create
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                }).build();
        //Open DB and work with it
        DataBaseHelper data = new DataBaseHelper(this, DB_NAME, null, DB_VERSION);
        List<Element> listOfCitys = data.getAllElements();
        data.close();
        //Init and inflate NavigationView
        drawerResult = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBar(false)
                .withActionBarDrawerToggle(true)
                .withSelectedItem(CURRENT_LOCATION_ID)
                .withAccountHeader(headerResult)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_edit_locations)
                                .withIcon(FontAwesome.Icon.faw_plus_circle)
                                .withIdentifier(EDIT_LOCATIONS_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf")),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_current_location)
                                .withIcon(FontAwesome.Icon.faw_map_marker)
                                .withIdentifier(CURRENT_LOCATION_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf")),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_settings)
                                .withIcon(FontAwesome.Icon.faw_cogs)
                                .withIdentifier(SETTINGS_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf")),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_share_app)
                                .withIcon(FontAwesome.Icon.faw_share_alt)
                                .withIdentifier(SHARE_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf")),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_help)
                                .withIcon(FontAwesome.Icon.faw_question_circle)
                                .withIdentifier(HELP_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf")),
                        new SecondaryDrawerItem().withName(R.string.drawer_item_contact)
                                .withIcon(FontAwesome.Icon.faw_info_circle)
                                .withIdentifier(CONTACT_ID)
                                .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"))
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        switch (iDrawerItem.getIdentifier()){
                            case EDIT_LOCATIONS_ID:
                                break;
                            case CURRENT_LOCATION_ID:
                                break;
                            case SETTINGS_ID:
                                break;
                            case SHARE_ID:
                                break;
                            case HELP_ID:
                                break;
                            case CONTACT_ID:
                                break;
                            default:
                                WeatherRequest mWeatherRequest = new WeatherRequest(String.valueOf(iDrawerItem.getIdentifier())); //TODO: погода по woeid
                                getSpiceManager().execute(mWeatherRequest, new WeatherRequestListener());
                                break;
                        }
                        return false;
                    }
                }).build();
        for (Element a : listOfCitys){
            drawerResult.addItem(new PrimaryDrawerItem()
                    .withName(a.getName())
                    .withIcon(FontAwesome.Icon.faw_map_marker)
                    .withTypeface(Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf"))
                    .withIdentifier(Integer.parseInt(a.getWoeid())),POSITION_INSERT);
        }
    }

    private final class WeatherRequestListener implements RequestListener<Weather> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
        }

        @Override
        public void onRequestSuccess(Weather result) {
            if (result != null){
                clearScreen();
                inflateScreen(result.getQuery().getResults().getChannel());
            }
        }
    }

}

//    findViewById(R.id.sign_in_button).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View v) {
//        Log.e("","click");
//        getGoogleApiClient().connect();
////                drawerResult.removeItem(1);
////                drawerResult.addItem(new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(FontAwesome.Icon.faw_home).withBadge("00000").withIdentifier(1));
//        headerResult.addProfiles(new ProfileDrawerItem().withName("Mikhail Mazurkevich").withEmail("mazumisha@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile)));
//        }
//        });



//
////        LatitudeLongitudeRequest mWeatherRequest = new LatitudeLongitudeRequest();
////        getSpiceManager().execute(mWeatherRequest, new WeatherRequestListener()); //TODO: woeid по широте и долготе
//
//SearchRequest mWeatherRequest = new SearchRequest();
//    getSpiceManager().execute(mWeatherRequest, new WeatherRequestListener()); //TODO: поиск woeid по названию города
//
////
//




//        WeatherRequest mWeatherRequest = new WeatherRequest();
//        mSpiceManager.execute(mWeatherRequest,new WeatherRequestListener());