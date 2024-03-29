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


class MyCell(val x:Int, val y:Int, val living:Boolean) extends FlowPanel {

	contents += new Label("")
	border = LineBorder.createGrayLineBorder
		
	
	background = {if (living) Color.BLACK else Color.WHITE}
	
	listenTo(mouse.clicks) 
	reactions += {
		case e: MouseClicked =>{Main.changeCellStatus(x, y, living)}
	}
	
	def this(x:Int, y:Int, previousWorld:Array[Array[Boolean]], init:Boolean) = this(x,y,{
		if (init) previousWorld(y)(x) else {
			val aliveNeighborsCounter = 
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX - 1) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 0) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY - 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 0) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX)) 1 else 0) +
					(if (previousWorld((y + MyCell.worldY + 1) % MyCell.worldY)((x + MyCell.worldX + 1) % MyCell.worldX)) 1 else 0);


			if (previousWorld(y)(x) && aliveNeighborsCounter < 2) false
			else if (previousWorld(y)(x) && aliveNeighborsCounter > 3) false
			else if (!previousWorld(y)(x) && aliveNeighborsCounter == 3) true
			else previousWorld(y)(x)}
	})

}