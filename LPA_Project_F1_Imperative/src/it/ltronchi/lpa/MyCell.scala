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


class MyCell(val x:Int, val y:Int) extends FlowPanel {
		
	contents += new Label("")
	border = LineBorder.createGrayLineBorder
	
	var _living = false
	background = Color.WHITE
	
	listenTo(mouse.clicks) 
	reactions += {
		case e: MouseClicked =>{
			living match {
			case true =>living = false;background = Color.WHITE
			case _=>living = true;background = Color.BLACK
			}
			repaint()
		}
	}
	
	def living = _living
	def living_= (alive:Boolean) = _living = {
		if (alive) {
			background = Color.BLACK
		} else {
			background = Color.WHITE
		}
		alive
	}
	
	def updateState(previousWorld: Array[Array[Boolean]]) {
		var neighbors =  Set.empty[Array[Int]]
		for (i <- -1 to 1) {
			for (j <- -1 to 1) {
				if (!(i==0 && j==0))
					neighbors += Array((x + MyCell.worldX + i) % MyCell.worldX, (y + MyCell.worldY + j) % MyCell.worldY)
			}
		}
		var aliveNeighborsCounter = 0;
		
		neighbors foreach(item => if (previousWorld(item(1))(item(0))) aliveNeighborsCounter += 1 )
		if (living && aliveNeighborsCounter < 2) living = false
		if (living && aliveNeighborsCounter > 3) living = false
		if (!living && aliveNeighborsCounter == 3) living = true 
	}
	
}