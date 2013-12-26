package sf.browser.model;

import java.util.Comparator;

import sf.browser.model.items.UrlSuggestionItem;

public class UrlSuggestionItemComparator implements
		Comparator<UrlSuggestionItem> {

	@Override
	public int compare(UrlSuggestionItem lhs, UrlSuggestionItem rhs) {
		Float value1 = new Float(lhs.getNote());
		Float value2 = new Float(rhs.getNote());
		return value2.compareTo(value1);
	}
}
