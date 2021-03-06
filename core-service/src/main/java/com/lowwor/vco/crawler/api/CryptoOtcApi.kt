package com.lowwor.vco.crawler.api

import com.lowwor.vco.crawler.mapper.*
import com.lowwor.vco.crawler.model.*
import com.lowwor.vco.crawler.model.price.OtcPriceItem
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables.zip


/**
 * Created by lowwor on 2018/1/23.
 */

class CryptoOtcApi constructor(private val cryptoOtcService: CryptoOtcService) {


    fun getUsdtPrices(): Observable<List<OtcPriceItem>> {
        return zip(getGankOtcUsdt(), getHuobiOtcUsdt(),
                getOtcbtcOtcUsdt(), { gankPriceRsp, huoBiPriceRsp, otcbtcPriceRsp ->


            listOf(GankMapper.convertToOneStepUsdtEntrancePrice(gankPriceRsp),
                    HuobiMapper.convertToOneStepUsdtEntrancePrice(huoBiPriceRsp),
                    OtcbtcMapper.convertToOneStepUsdtEntrancePrice(otcbtcPriceRsp[0])
            )
        })
    }

    fun getBtcPrices(): Observable<List<OtcPriceItem>> {
        return zip(getHuobiOtcUsdt(), getHuobiTickerBtc(),
                getGankOtcUsdt(), getGankTickerBtc(),
                getHuobiOtcBtc(), getOtcbtcOtcBtc(),
                getZbTickerBtc(), getLocalBitcoinOtcBtc(), { huobiOtcUsdtRsp, huobiTickerBtcRsp,
                                                             gankOtcUsdtRsp, gankTickerBtcRsp,
                                                             huoBiPriceRsp, otcbtcPriceRsp, zbPriceRsp,
                                                             localBitcoinsPriceRsp ->

            listOf(HuobiMapper.convertToTwoStepBtcToEntrancePrice(huobiOtcUsdtRsp, huobiTickerBtcRsp),
                    HuobiMapper.convertToOneStepBtcEntrancePrice(huoBiPriceRsp),
                    GankMapper.convertToTwoStepBtcEntrancePrice(gankOtcUsdtRsp, gankTickerBtcRsp),
                    OtcbtcMapper.convertToOneStepBtcEntrancePrice(otcbtcPriceRsp[0]),
                    ZbMapper.convertToQcBtcEntrancePrice(zbPriceRsp),
                    LocalBitcoinMapper.convertToOneStepBtcEntrancePrice(localBitcoinsPriceRsp)
            )
        })
    }


    private fun getHuobiOtcUsdt(): Observable<HuobiOtcRsp> =
            cryptoOtcService.getHuobiOtcPrice(HUOBI_OTC_COIN_ID_USDT)

    private fun getHuobiTickerBtc(): Observable<HuobiTickerRsp> =
            cryptoOtcService.getHuobiTickerPrice(HUOBI_TICKER_BTC_USDT)

    private fun getHuobiOtcBtc(): Observable<HuobiOtcRsp> =
            cryptoOtcService.getHuobiOtcPrice(HUOBI_OTC_COIN_ID_BTC)

    private fun getOtcbtcOtcUsdt(): Observable<List<OtcbtcPriceItemRsp>> =
            cryptoOtcService.getOtcbtcOtcPrice(OTCBTC_OTC_CURRENCY_USDT)

    private fun getOtcbtcOtcBtc(): Observable<List<OtcbtcPriceItemRsp>> =
            cryptoOtcService.getOtcbtcOtcPrice(OTCBTC_OTC_CURRENCY_BTC)

    private fun getZbTickerBtc(): Observable<ZbTickerRsp> =
            cryptoOtcService.getZbTickerPrice(ZB_TICKER_BTC_QC)

    private fun getGankOtcUsdt(): Observable<GankOtcRsp> =
            cryptoOtcService.getGankOtcUsdtPrice(GANK_OTC_SYMBOL_USDT)

    private fun getGankTickerBtc(): Observable<GankTickerRsp> =
            cryptoOtcService.getGankTickerPrice(GANK_TICKER_BTC_USDT)

    private fun getLocalBitcoinOtcBtc(): Observable<LocalBitcoinOtcRsp> =
            cryptoOtcService.getLocalBitcoinOtcPrice()


}