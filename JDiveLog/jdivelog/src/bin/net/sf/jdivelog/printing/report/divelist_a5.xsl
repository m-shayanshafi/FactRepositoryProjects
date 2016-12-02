<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:fo="http://www.w3.org/1999/XSL/Format" xmlns:java="http://xml.apache.org/xslt/java" xmlns:uc="net.sf.jdivelog.util.UnitConverter" xmlns:df="java.text.DecimalFormat" exclude-result-prefixes="java,uc,df">
<xsl:param name="pagewidth">148.5mm</xsl:param>
<xsl:param name="pageheight">210mm</xsl:param>
<xsl:param name="margin-page-right">10mm 7mm 15mm 15mm</xsl:param>
<xsl:param name="margin-page-left">10mm 7mm 15mm 15mm</xsl:param>
<xsl:param name="margin-body-right">10mm 7mm 15mm 15mm</xsl:param>
<xsl:param name="margin-body-left">10mm 7mm 15mm 15mm</xsl:param>
<xsl:param name="font-family">Arial</xsl:param>
<xsl:param name="font-size-text">8pt</xsl:param>
<xsl:template match="/">
	<fo:root>
		<fo:layout-master-set>
			<fo:simple-page-master margin="{$margin-page-right}" master-name="template-page-right">
				<xsl:attribute name="page-height"><xsl:value-of select="$pageheight"/></xsl:attribute>
				<xsl:attribute name="page-width"><xsl:value-of select="$pagewidth"/></xsl:attribute>
				<fo:region-body margin="{$margin-body-right}"/>
				<fo:region-before region-name="content-right-before" extent="10mm" display-align="before"/>
			</fo:simple-page-master>
			<fo:simple-page-master margin="{$margin-page-left}" master-name="template-page-left">
				<xsl:attribute name="page-height"><xsl:value-of select="$pageheight"/></xsl:attribute>
				<xsl:attribute name="page-width"><xsl:value-of select="$pagewidth"/></xsl:attribute>
				<fo:region-body margin="{$margin-body-left}"/>
				<fo:region-before region-name="content-left-before" extent="10mm" display-align="before"/>
			</fo:simple-page-master>
			<fo:page-sequence-master master-name="content-pages">
				<fo:repeatable-page-master-alternatives>
					<fo:conditional-page-master-reference master-reference="template-page-right" odd-or-even="odd"/>
					<fo:conditional-page-master-reference master-reference="template-page-left" odd-or-even="even"/>
				</fo:repeatable-page-master-alternatives>
			</fo:page-sequence-master>
		</fo:layout-master-set>
		<fo:page-sequence master-reference="content-pages">
			<fo:static-content flow-name="content-right-before">
				<fo:block text-align="right">
				</fo:block>
			</fo:static-content>
			<fo:static-content flow-name="content-left-before">
				<fo:block text-align="left">
				</fo:block>
			</fo:static-content>
			<fo:flow flow-name="xsl-region-body">
				<fo:block font-family="{$font-family}" font-size="{$font-size-text}">
					<fo:table table-layout="fixed" width="100%">
						<fo:table-column column-width="9mm"/>
						<fo:table-column column-width="25mm"/>
						<fo:table-column column-width="40mm"/>
						<fo:table-column column-width="9mm"/>
						<fo:table-column column-width="9mm"/>
						<fo:table-header>
							<fo:table-row>
							<fo:table-cell>
								<fo:block><xsl:value-of select="java:net.sf.jdivelog.gui.resources.Messages.getString('dive_no')"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block><xsl:value-of select="java:net.sf.jdivelog.gui.resources.Messages.getString('date')"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block><xsl:value-of select="java:net.sf.jdivelog.gui.resources.Messages.getString('spot')"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block><xsl:value-of select="java:net.sf.jdivelog.gui.resources.Messages.getString('depth')"/></fo:block>
							</fo:table-cell>
							<fo:table-cell>
								<fo:block><xsl:value-of select="java:net.sf.jdivelog.gui.resources.Messages.getString('duration')"/></fo:block>
							</fo:table-cell>
							</fo:table-row>
						</fo:table-header>
						<fo:table-body>
							<xsl:apply-templates/>
						</fo:table-body>
					</fo:table>
				</fo:block>
			</fo:flow>
		</fo:page-sequence>
	</fo:root>
</xsl:template>
<xsl:template match="JDiveLog/JDive">
	<fo:table-row>
		<xsl:call-template name="DiveNum">
			<xsl:with-param name="num" select="DiveNum"/>
		</xsl:call-template>
		<xsl:call-template name="Date">
			<xsl:with-param name="year" select="DATE/YEAR"/>
			<xsl:with-param name="month" select="DATE/MONTH"/>
			<xsl:with-param name="day" select="DATE/DAY"/>
			<xsl:with-param name="hour" select="TIME/HOUR"/>
			<xsl:with-param name="minute" select="TIME/MINUTE"/>
		</xsl:call-template>
		<xsl:call-template name="DiveSite">
			<xsl:with-param name="id" select="diveSiteId"/>
		</xsl:call-template>
		<xsl:variable name="diveunits" select="java:net.sf.jdivelog.util.UnitConverter.getSystem(UNITS)"/>
		<xsl:variable name="displayunits" select="java:net.sf.jdivelog.util.UnitConverter.getDisplaySystem()"/>
		<xsl:call-template name="Depth">
			<xsl:with-param name="depth" select="number(Depth)"/>
			<xsl:with-param name="diveunits" select="$diveunits"/>
			<xsl:with-param name="displayunits" select="$displayunits"/>
		</xsl:call-template>
		<xsl:call-template name="Duration">
			<xsl:with-param name="duration" select="number(Duration)"/>
			<xsl:with-param name="diveunits" select="$diveunits"/>
			<xsl:with-param name="displayunits" select="$displayunits"/>
		</xsl:call-template>
		<xsl:apply-templates/>
	</fo:table-row>
	<xsl:choose>
	<xsl:when test="position() mod 28 = 10">
	<fo:table-row>
		<fo:table-cell number-columns-spanned="5">
			<fo:block text-align="center" margin="8mm 8mm 8mm 8mm">
				<xsl:variable name="file" select="java:net.sf.jdivelog.printing.report.ImageUtil.getNextImage()"/>
				<fo:external-graphic src="{$file}">
					<xsl:attribute name="content-height">40mm</xsl:attribute>
				</fo:external-graphic>				
			</fo:block>
		</fo:table-cell>
	</fo:table-row>
	</xsl:when>
	</xsl:choose>
</xsl:template>
<xsl:template name="DiveNum">
	<xsl:param name="num"/>
	<fo:table-cell>
		<fo:block>
			<xsl:value-of select="$num"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>
<xsl:template name="Date">
	<xsl:param name="year"/>
	<xsl:param name="month"/>
	<xsl:param name="day"/>
	<xsl:param name="hour"/>
	<xsl:param name="minute"/>
	<fo:table-cell>
		<fo:block>
			<xsl:value-of select="java:net.sf.jdivelog.util.DateFormatUtil.formatDateTime($year,$month,$day,$hour,$minute)"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>
<xsl:template name="Depth">
	<xsl:param name="depth"/>
	<xsl:param name="diveunits"/>
	<xsl:param name="displayunits"/>
	<xsl:variable name="uc" select="java:net.sf.jdivelog.util.UnitConverter.newInstance($diveunits,$displayunits)"/>
	<fo:table-cell>
		<fo:block>
			<xsl:value-of select="uc:formatAltitude($uc, $depth, '##0.0')"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>
<xsl:template name="Duration">
	<xsl:param name="duration"/>
	<xsl:param name="diveunits"/>
	<xsl:param name="displayunits"/>
	<xsl:variable name="uc" select="java:net.sf.jdivelog.util.UnitConverter.newInstance($diveunits,$displayunits)"/>
	<fo:table-cell>
		<fo:block>
			<xsl:value-of select="uc:formatTime($uc, $duration, '##0.0')"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>
<xsl:template name="DiveSite">
	<xsl:param name="id"/>
	<fo:table-cell>
		<fo:block>
			<xsl:value-of select="/JDiveLog/Masterdata/DiveSites/DiveSite[privateId=$id]/spot"/>
		</fo:block>
	</fo:table-cell>
</xsl:template>
</xsl:stylesheet>