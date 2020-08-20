package com.kine.policies

/**
 * Retry policy for a request.
 */
interface RetryPolicy {


    /**
     * Returns the current timeout (used for logging).
     */
    fun getCurrentTimeout(): Int

    /**
     * Returns the total retry count (used for logging).
     */
    fun getRetryCount(): Int

    /**
     * Returns the total retry count (used for logging).
     */
    fun setCurrentTimeout(timeOut: Int)

    /**
     * Returns the backoff multiplier for the policy.
     */
    fun getBackoffMultiplier(): Float

    fun isSame(retryPolicy: RetryPolicy): Boolean {
        return (getCurrentTimeout() == retryPolicy.getCurrentTimeout() && getRetryCount() == retryPolicy.getRetryCount() &&
            getBackoffMultiplier() == retryPolicy.getBackoffMultiplier())
    }

}