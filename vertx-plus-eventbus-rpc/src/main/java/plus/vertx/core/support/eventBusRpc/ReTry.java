package plus.vertx.core.support.eventBusRpc;

import io.vertx.core.Future;
import io.vertx.core.Promise;

/**
 * 重试机制
 */
public class ReTry {
    /**
     * 循环调用 [block]，直到返回结果为成功或重试次数[retryTimes]用尽
     * @param retryTimes
     * @param retryIntervalMs
     * @return
     */
    public Future<Boolean> retryWhenFail(Integer retryTimes,Long retryIntervalMs) {
        Promise<Boolean> promise = Promise.promise();
        // boolean retry = false;
        return promise.future();
    }
/* fupun retryWhenFail(retryTimes: Int, retryIntervalMs: Long? = null, block: () -> Future<Boolean>): Future<Boolean> {
    // 预设第0次调用为失败，触发重试逻辑，等于间接的第一次执行
    var future = Future.succeededFuture(false)
    var retry = false

    // 重试逻辑lambda
    val retryBlock = {
        val blockResult: Future<Boolean>
        // 带延迟的重试
        if (retry && retryIntervalMs != null && retryIntervalMs > 0) {
            blockResult = Future.future<Boolean> { timerPromise ->
                VertxInstance.vertx.setTimer(retryIntervalMs) {
                    // 执行block()
                    block()
                        //  block()结果传递到timerPromise中（回调方式）
                        .onComplete(timerPromise)
                }
            }
        }
        // 首次执行或不带延迟的重试
        else {
            blockResult = block()
            retry = true
        }
        blockResult
    }

    // 因响应式特殊的回调方式，所以一次性注册所有重试次数的逻辑
    for (index in 0 until retryTimes) {
        // 注册重试逻辑
        future = future.compose(
            { lastBlockResult ->
                // 若上次执行为成功结果，则向后传递成功结果
                if (true == lastBlockResult) {
                    Future.succeededFuture(lastBlockResult)
                }
                // 若上次执行为失败结果，则执行重试逻辑
                else {
                    retryBlock()
                }
            }, {
                // 若上次执行出现异常，则执行重试逻辑
                retryBlock()
            }
        )
    }
    return future
} */
}
