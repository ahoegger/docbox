package ch.ahoegger.docbox.client.search;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractSearchOutline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.ahoegger.docbox.shared.Icons;

/**
 * <h3>{@link SearchOutline}</h3>
 *
 * @author Andreas Hoegger
 */
@Order(2000)
public class SearchOutline extends AbstractSearchOutline {

	private static final Logger LOG = LoggerFactory.getLogger(SearchOutline.class);

	@Override
	protected void execSearch(final String query) {
		LOG.info("Search started");
		// TODO [aho]: Implement search
	}

	@Override
	protected String getConfiguredIconId() {
		return Icons.Search;
	}
}
