
			var api;
		
			$(document).ready(function() {
				
				//Default carousel
				api = $("#smart-carousel").smartCarousel({
					itemWidth:170,
					itemHeight:250,
					distance:15,
					selectedItemDistance:50,
					selectedItemZoomFactor:1,
					unselectedItemZoomFactor:0.67,
					unselectedItemAlpha:0.6,
					motionStartDistance:170,
					topMargin:119,
					gradientStartPoint:0.35,
					gradientOverlayColor:"#f0f0f0",
					gradientOverlaySize:190,
					reflectionVisible:true,
					reflectionSize:70,
					selectByClick:true,
					autoSlideshow:true,
					autoSlideshowDelay:5000
				});
				
				//Transparent carousel
				$("#transparent-carousel").smartCarousel({
					itemWidth:201,
					itemHeight:351,
					distance:-10,
					selectedItemDistance:35,
					selectedItemZoomFactor:0.9,
					unselectedItemZoomFactor:0.6,
					unselectedItemAlpha:0.6,
					motionStartDistance:140,
					topMargin:30,
					gradientStartPoint:0.36,
					gradientOverlayColor:"#fafafa",
					gradientOverlaySize:190,
					selectByClick:true
				});
				
				//Round image carousel
				$("#round-image-carousel").smartCarousel({
					itemWidth:260,
					itemHeight:260,
					distance:12,
					selectedItemDistance:75,
					selectedItemZoomFactor:1,
					unselectedItemZoomFactor:0.5,
					unselectedItemAlpha:0.6,
					motionStartDistance:210,
					topMargin:115,
					gradientStartPoint:0.35,
					gradientOverlayColor:"#f0f0f0",
					gradientOverlaySize:190,
					selectByClick:true
				});
				
				//Square image carousel
				$("#square-image-carousel").smartCarousel({
					itemWidth:240,
					itemHeight:240,
					distance:10,
					selectedItemDistance:70,
					selectedItemZoomFactor:1,
					unselectedItemZoomFactor:0.5,
					unselectedItemAlpha:0.9,
					motionStartDistance:200,
					topMargin:140,
					gradientStartPoint:0.35,
					gradientOverlayColor:"#fafafa",
					gradientOverlaySize:190,
					selectByClick:false
				});
				
			});
		

