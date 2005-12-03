package wicket.extensions.markup.html.form.select;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import wicket.WicketRuntimeException;
import wicket.extensions.markup.html.repeater.OrderedRepeatingView;
import wicket.markup.ComponentTag;
import wicket.markup.MarkupStream;
import wicket.markup.html.WebMarkupContainer;
import wicket.model.IModel;
import wicket.model.Model;

/**
 * Component that makes it easy to produce a list of SelectOption components
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public class SelectOptions extends OrderedRepeatingView
{
	private static final long serialVersionUID = 1L;
	private boolean recreateChoices = false;
	private IOptionRenderer renderer;

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param model
	 * @param renderer
	 */
	public SelectOptions(String id, IModel model, IOptionRenderer renderer)
	{
		super(id, model);
		this.renderer = renderer;
		setRenderBodyOnly(true);
	}

	/**
	 * Constructor
	 * 
	 * @param id
	 * @param elements 
	 * @param renderer
	 */
	public SelectOptions(String id, Collection elements, IOptionRenderer renderer)
	{
		this(id, new Model((Serializable)elements), renderer);
	}

	/**
	 * Controls whether or not SelectChoice objects are recreated every request
	 * 
	 * @param refresh
	 * @return this for chaining
	 */
	public SelectOptions setRecreateChoices(boolean refresh)
	{
		this.recreateChoices = refresh;
		return this;
	}

	protected void onBeginRequest()
	{
		if (size() == 0 || recreateChoices)
		{
			// populate this repeating view with SelectOption components
			removeAll();

			Object modelObject = getModelObject();

			if (modelObject != null)
			{
				if (!(modelObject instanceof Collection))
				{
					// TODO
					throw new WicketRuntimeException("model not a collection");
				}

				// iterator over model objects for SelectOption components
				Iterator it = ((Collection)modelObject).iterator();

				while (it.hasNext())
				{
					// we need a container to represent a row in repeater
					WebMarkupContainer row = new WebMarkupContainer(newChildId());
					row.setRenderBodyOnly(true);
					add(row);

					// we add our actual SelectOption component to the row
					Object value = it.next();
					String text = renderer.getDisplayValue(value);
					IModel model = renderer.getModel(value);
					row.add(new SimpleSelectOption("option", model, text));
				}
			}
		}
	}

	private static class SimpleSelectOption extends SelectOption
	{

		private String text;

		/**
		 * @param id
		 * @param model
		 * @param text
		 */
		public SimpleSelectOption(String id, IModel model, String text)
		{
			super(id, model);
			this.text = text;
		}

		protected void onComponentTagBody(MarkupStream markupStream, ComponentTag openTag)
		{
			replaceComponentTagBody(markupStream, openTag, text);
		}


		private static final long serialVersionUID = 1L;


	}
}
