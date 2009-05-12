/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.wicket.markup.parser.filter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.MarkupElement;
import org.apache.wicket.markup.parser.AbstractMarkupFilter;
import org.apache.wicket.markup.parser.XmlTag;

/**
 * MarkupFilter that expands certain open-close tag as separate open and close tags. Firefox, unless
 * it gets text/xml mime type, treats these open-close tags as open tags which results in corrupted
 * DOM. This happens even with xhtml doctype.
 * 
 * In addition, some tags are required open-body-close for Wicket to work properly.
 * 
 * @author Juergen Donnerstag
 * @author Matej Knopp
 */
public class OpenCloseTagExpander extends AbstractMarkupFilter
{
	private static final List<String> replaceForTags = Arrays.asList(new String[] { "div", "span",
			"p", "strong", "b", "e", "select" });

	private ComponentTag next = null;

	/**
	 * 
	 * @see org.apache.wicket.markup.parser.IMarkupFilter#nextTag()
	 */
	public MarkupElement nextTag() throws ParseException
	{
		if (next != null)
		{
			MarkupElement tmp = next;
			next = null;
			return tmp;
		}

		ComponentTag tag = nextComponentTag();
		if (tag == null)
		{
			return tag;
		}

		if (tag.isOpenClose())
		{
			String name = tag.getName();
			if (tag.getNamespace() != null)
			{
				name = tag.getNamespace() + ":" + tag.getName();
			}

			if (replaceForTags.contains(name.toLowerCase()))
			{
				tag.setType(XmlTag.OPEN);
				tag.setModified(true);

				next = new ComponentTag(tag.getName(), XmlTag.CLOSE);
				next.setNamespace(tag.getNamespace());
				next.setOpenTag(tag);
				next.setModified(true);
			}
		}
		return tag;
	}
}
