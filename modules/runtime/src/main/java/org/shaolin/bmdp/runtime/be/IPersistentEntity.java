package org.shaolin.bmdp.runtime.be;

/**
 * Interface of Business Entity which needs to be persistent.
 * 
 */
public interface IPersistentEntity extends IBusinessEntity {
	public final static String ENABLE = "_enable";
	public final static long TIMESTAMP_INITVAL = 0L;
	public final static long TIMESTAMP_MAXVAL = Long.MAX_VALUE;

	/**
	 * Primary key.
	 * 
	 * @return
	 */
	public long getId();
	
	/**
	 * Enable this record.
	 * 
	 * @return
	 */
	public boolean isEnabled();
	
	/**
	 * Whether enable this record or not.
	 * 
	 * @param isEnabled
	 */
	public void setEnabled(boolean isEnabled);
	
	/**
	 * Get create date of this item.
	 * 
	 * @return
	 */
	public java.util.Date getCreateDate();
	
	/**
	 * 
	 * @param createDate
	 */
	public void setCreateDate(java.util.Date createDate);
}
