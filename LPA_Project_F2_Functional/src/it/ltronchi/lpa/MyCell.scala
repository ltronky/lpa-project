package it.ltronchi.lpa

import scala.swing.Component
import java.awt.Graphics2D
import java.awt.Color
import scala.swing.event.MousePressed
import java.awt.Dimension
import scala.swing._
import javax.swing.border.LineBorder
import scala.swing.event.MouseClicked
import java.awt.Font
import scala.collection.Set

object MyCell {
	var worldX = 0;
	var worldY = 0;
}


class MyCell(val x:Int, val y:Int, val living:Int) extends FlowPanel {

	contents += new Label("")
	border = LineBorder.createGrayLineBorder
	
	background = {
		living match {
		case 0 => Color.WHITE
		case 1|2 => Color.GREEN
		case 3|4|5 => Color.RED
		case _ => Color.BLACK
		}
	}

	
	listenTo(mouse.clicks) 
	reactions += {
		case e: MouseClicked =>{Main.changeCellStatus(x, y, living)}
	}
	
	def this(x:Int, y:Int, previousWorld:Array[Array[Int]], init:Boolean) = this(x,y,{
		if (init) previousWorld(y)(x) else {
			val youngNeighborsCounter = 
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 2) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 1 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 2) 1 else 0);
			
			val adultNeighborsCounter = 
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 5) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 3 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 4 ||
					    previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) == 5) 1 else 0);

			val oldNeighborsCounter = 
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) >= 6) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX) >= 6) 1 else 0);

			val totalLivingNeighbors = youngNeighborsCounter + adultNeighborsCounter + oldNeighborsCounter;

			
			previousWorld(y)(x) match {
			case 0 => if (totalLivingNeighbors == 3 && oldNeighborsCounter <= 1) previousWorld(y)(x) +1 else 0
			case 1|2|3|4|5 => if (totalLivingNeighbors < 2 || totalLivingNeighbors > 3) 0 else previousWorld(y)(x) + 1
			case _ => if (totalLivingNeighbors != 3) 0 else previousWorld(y)(x) +1
			}
		}
	})

}