package com.kine.policies

/**
 * Default retry policy for requests.
 */
class DefaultRetryPolicy
/**
 * Constructs a new retry policy using the default timeouts.
 */ @JvmOverloads constructor(
    /** The current timeout in milliseconds.  */
    private var mCurrentTimeoutMs: Int = DEFAULT_TIMEOUT_MS,
    /** The maximum number of attempts.  */
    private val mMaxNumRetries: Int = DEFAULT_MAX_RETRIES,
    /** The backoff multiplier for the policy.  */
    private val mBackoffMultiplier: Float = DEFAULT_BACKOFF_MULT
) : RetryPolicy {

    /**
     * Returns the current timeout.
     */
    override fun getCurrentTimeout(): Int {
        return mCurrentTimeoutMs
    }

    /**
     * Returns the current retry count.
     */
    override fun getRetryCount(): Int {
        return mMaxNumRetries
    }

    /**
     * Returns the total retry count (used for logging).
     *
     * @param timeOut
     */
    override fun setCurrentTimeout(timeOut: Int) {
        mCurrentTimeoutMs = timeOut
    }

    /**
     * Returns the backoff multiplier for the policy.
     */
    override fun getBackoffMultiplier(): Float {
        return mBackoffMultiplier
    }

    companion object {
        /** The default socket timeout in milliseconds  */
        const val DEFAULT_TIMEOUT_MS = 12500

        /** The default number of retries  */
        const val DEFAULT_MAX_RETRIES = 1

        /** The default backoff multiplier  */
        const val DEFAULT_BACKOFF_MULT = 1f
    }
    /**
     * Constructs a new retry policy.
     * @param mCurrentTimeoutMs The initial timeout for the policy.
     * @param mMaxNumRetries The maximum number of retries.
     * @param mBackoffMultiplier Backoff multiplier for the policy.
     */
}