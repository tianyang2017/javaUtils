package utils.beans;

import java.util.List;

/**
 * @author : tianyang
 * @description :
 * @date :2018年10月30日
 */
public class Response<T,V> {
	private RequHead head;
	private T main;
	private List<V> details;


	/**
	 * @return the head
	 */


	/**
	 * @return the main
	 */
	public T getMain() {
		return main;
	}

	public RequHead getHead() {
		return head;
	}
	public void setHead(RequHead head) {
		this.head = head;
	}
	/**
	 * @param main the main to set
	 */
	public void setMain(T main) {
		this.main = main;
	}

	/**
	 * @return the details
	 */
	public List<V> getDetails() {
		return details;
	}

	/**
	 * @param details the details to set
	 */
	public void setDetails(List<V> details) {
		this.details = details;
	}

}
