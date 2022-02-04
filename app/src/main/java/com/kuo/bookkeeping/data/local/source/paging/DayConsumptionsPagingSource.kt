package com.kuo.bookkeeping.data.local.source.paging

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Page
import androidx.paging.PagingState
import com.kuo.bookkeeping.data.local.dao.ConsumptionDao
import com.kuo.bookkeeping.data.local.model.ConsumptionCategoryTuple

typealias DayConsumptions = Pair<String, List<ConsumptionCategoryTuple>>

class DayConsumptionsPagingSource(
    private val consumptionDao: ConsumptionDao
) : PagingSource<Int, DayConsumptions>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, DayConsumptions> {
        return try {
            println("paging load key: ${params.key}")
            println("paging load loadSize: ${params.loadSize}")


            val items = consumptionDao.getConsumptionsGroupByDate(
                start = params.key ?: 0,
                limit = params.loadSize
            ).map {
                it.toPair()
            }

            println("paging load items: $items")

            println("paging load prev key: ${params.key?.minus(params.loadSize)}")

            val nextKey = when {
                params.key == null -> {
                    params.loadSize
                }
                items.size < params.loadSize -> {
                    null
                }
                else -> {
                    params.key?.plus(params.loadSize)
                }
            }

            println("paging load next key: $nextKey")

            Page(
                data = items,
                prevKey = params.key?.minus(params.loadSize),
                nextKey = nextKey
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, DayConsumptions>): Int? {
        state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            println("paging load refresh key: anchorPage: $anchorPage")
        }


        return null
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            val pageSize = state.config.pageSize
//            println("paging load get refresh key: page size: $pageSize")
//            println("paging load get refresh key: ${anchorPage?.prevKey?.plus(pageSize) ?: anchorPage?.nextKey?.minus(pageSize)}")
//            anchorPage?.prevKey?.plus(pageSize) ?: anchorPage?.nextKey?.minus(pageSize)
//        }
    }
}