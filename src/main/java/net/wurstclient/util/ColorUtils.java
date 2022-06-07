/*
 * Copyright (c) 2014-2022 Wurst-Imperium and contributors.
 *
 * This source code is subject to the terms of the GNU General Public
 * License, version 3. If a copy of the GPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/gpl-3.0.txt
 */
package net.wurstclient.util;

import java.awt.Color;

import net.minecraft.util.math.MathHelper;
import net.wurstclient.util.json.JsonException;

public enum ColorUtils
{
	;
	
	public static String toHex(Color color)
	{
		return String.format("#%02x%02x%02x%02x", color.getAlpha(),color.getRed(), color.getGreen(), color.getBlue());
		//return String.format("#%08X", (color.getRGB() & 0x00FFFFFF) | (color.getAlpha() & 0xFF000000));
	}
	
	public static Color parseHex(String s) throws JsonException
	{
		if(!s.startsWith("#"))
			throw new JsonException("Missing '#' prefix.");
		
		if(s.length() != 9)
			throw new JsonException(
				"Expected String of length 9, got " + s.length() + " instead.");
		
		int[] argb = new int[4];
		
		try
		{
			for(int i = 0; i < argb.length; i++)
			{
				String channelString = s.substring(i * 2 + 1, i * 2 + 3);
				int channel = Integer.parseUnsignedInt(channelString, 16);
				argb[i] = MathHelper.clamp(channel, 0, 255);
			}
			
		}catch(NumberFormatException e)
		{
			throw new JsonException(e);
		}
		
		return new Color(argb[1], argb[2], argb[3], argb[0]);
	}
	
	public static Color tryParseHex(String s)
	{
		try
		{
			return parseHex(s);
			
		}catch(JsonException e)
		{
			return null;
		}
	}
	
	public static Color parseRGBA(String red, String green, String blue, String alpha)
		throws JsonException
	{
		String[] rgbaStrings = {red, green, blue, alpha};
		int[] rgba = new int[4];
		
		try
		{
			for(int i = 0; i < rgba.length; i++)
			{
				int channel = Integer.parseInt(rgbaStrings[i]);
				rgba[i] = MathHelper.clamp(channel, 0, 255);
			}
			
		}catch(NumberFormatException e)
		{
			throw new JsonException(e);
		}
		
		return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);
	}
	
	public static Color tryParseRGBA(String red, String green, String blue, String alpha)
	{
		try
		{
			return parseRGBA(red, green, blue, alpha);
			
		}catch(JsonException e)
		{
			return null;
		}
	}
}
