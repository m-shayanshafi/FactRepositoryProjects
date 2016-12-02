<!-- The IHM template -->


<?php
     
     defined( '_VALID_MOS' ) or die( 'Direct Access to this location is not allowed.' );
     
     
     $iso = split( '=', _ISO );
     
     echo '<?xml version="1.0" encoding="'. $iso[1] .'"?' .'>';
     
     ?>
     
     <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
     
     <html xmlns="http://www.w3.org/1999/xhtml">
       
       <!-- HTML Header -->
       
       <head>
	 
	 <?php if ( $my->id ) initEditor(); ?>
	 
	 <meta http-equiv="Content-Type" content="text/html; <?php echo _ISO; ?>" />
	 
	 <?php mosShowHead(); ?>
	 
	 <link rel="shortcut icon" href="<?php echo $mosConfig_live_site;?>/images/favicon.ico" />
	 
	 <link rel="stylesheet" type="text/css" href="<?php echo $mosConfig_live_site; ?>/templates/ihm/css/template_css.css" />
	 
       </head>
       
       
       
       <body>
	 
	 <!-- Top  -->
	 
	 <a name="up" id="up"></a>
	 
	 <!-- Title bar, with path & time -->
	 
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   
	   <tr bgcolor="#000000">
	     
	     <td class="navigationline" height="20" valign="middle">&nbsp;&nbsp;<?php include $GLOBALS['mosConfig_absolute_path'] . '/pathway.php'; ?></td>
	     
	     <td class="navigationline" height="20" align="right" valign="middle"><?php echo (strftime ("%A, %d %B %Y", time()+($mosConfig_offset*60*60))); ?>&nbsp;&nbsp;</td>
	     
	   </tr>
	   
	 </table>
	 
	 
	 
	 <!-- Header with logo & shadow  -->
	 
	 <table width="100%" border="0" cellpadding="0" cellspacing="0">
	   
	   <tr>
	     
	     <td height="150" valign="center" bgcolor="#95CBE9" background="<?php echo $mosConfig_live_site;?>/templates/ihm/images/headermainbg.png"></td>
	     
	   </tr>
	   
	   <tr>
	     
	     <td valign="top" background="<?php echo $mosConfig_live_site;?>/templates/ihm/images/topshadow.gif"><img src="<?php echo $mosConfig_live_site;?>/templates/ihm/images/topshadow.gif" width="10" height="14"></td>
	       
	     </tr>
	     
	   </table>
	   
	   
	   
	   <!-- Main content -->
	   
	   <table border="0" cellpadding="5" cellspacing="0" width="100%">
	     
	     <tr>
	       
	       
	       <!-- LEFT Modules -->
	       
	       
	       <?php
		    
		    if (mosCountModules('left')>0) {
	       
	       ?>
	       
	       <td width="180" valign="top">
		 
		 <?php mosLoadModules ( "left" ); ?>
		 
	       </td>
	       
	       <?php
		    
		    }
		    
		    ?>
	       
	       
	       <!-- Main Content Section -->
	       
	       
	       <td valign="top">
		 
		 <?php if (mosCountModules('top')>0) mosLoadModules('top','true'); ?>
		 
		 
		 <?php mosMainBody(); ?>
		 
	       </td>
	       
	       
	       
	       <!-- RIGHT Modules -->
	       
	       <?php
		    
		    if (mosCountModules('right')>0) {
	       
	       ?>
	       
	       <td width="180" valign="top">
		 
		 <?php mosLoadModules ( "right" ); ?>
		 
	       </td>
	       
	       <?php
		    
		    }
		    
		    ?>
	       
	     </tr>
	     
	   </table>
	   
	   
	   
	   
	   <!-- Content footer -->
	   
	   
	   <div align="center">
	     
	     <img src="<?php echo $mosConfig_live_site;?>/templates/ihm/images/arrow2up.gif" width="12" height="9" border="0" align="middle">
	       
	       <font color="#999999" size="1" face="Verdana"><a href="<?php echo sefRelToAbs($_SERVER['REQUEST_URI']); ?>#up">top of page</a></font>
	       
	       <img src="<?php echo $mosConfig_live_site;?>/templates/ihm/images/arrow2up.gif" width="12" height="9" align="middle">
		 
	       </div>
	       
	       
	       
	       
	       <!-- Footer -->
	       
	       <table width="100%" border="0" cellpadding="0" cellspacing="0">
		 
		 <tr>
		   
		   <td valign="top" background="<?php echo $mosConfig_live_site;?>/templates/ihm/images/bottom_snow.jpg"><img src="<?php echo $mosConfig_live_site;?>/templates/ihm/images/bottom_snow.jpg"></td>
		     
		   </tr>
		   
		 </table>

		 
	       </body>
	       
</html>			