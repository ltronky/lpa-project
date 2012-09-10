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

// 0 -> bianco -> morta
// 1 -> verde -> giovane
// 2 -> verde -> giovane
// 3 -> rossa -> adulta
// 4 -> rossa -> adulta
// 5 -> rossa -> adulta
// 6+ -> nera -> vecchia

class MyCell(val x:Int, val y:Int) extends FlowPanel {
	
	
	size = new Dimension(10,10)
	contents += new Label("")
	
	border = LineBorder.createGrayLineBorder
	
	var _living = 0
	background = Color.WHITE
	
	listenTo(mouse.clicks) 
	reactions += {
		case e: MouseClicked =>{
			if (background == Color.WHITE) {
				living = 1
				background = Color.GREEN
			} else if (background == Color.GREEN) {
				living = 3
				background = Color.RED
			} else if (background == Color.RED) {
				living = 6
				background = Color.BLACK
			} else {
				living = 0
				background = Color.WHITE
			}
			repaint()
		}
	}
	
	def living = _living
	
	def living_= (alive:Int) = {
		_living = alive
		if (alive == 0) {
			background = Color.WHITE
		} else if (alive == 1 || alive == 2){
			background = Color.GREEN
		} else if (alive == 3 || alive == 4 || alive == 5){
			background = Color.RED
		} else {
		  background = Color.BLACK
		}
	}
	
	def updateState(previousWorld: Array[Array[Int]]) {
		var neighbors =  Set.empty[Array[Int]]
		for (i <- -1 to 1) {
			for (j <- -1 to 1) {
				if (!(i==0 && j==0))
					neighbors += Array((x + MyCell.worldX + i) % MyCell.worldX, (y + MyCell.worldY + j) % MyCell.worldY)
			}
		}
		var youngNeighborsCounter = 0;
		var adultNeighborsCounter = 0;
		var oldNeighborsCounter = 0;
		
		neighbors foreach(item => 
		if (previousWorld(item(1))(item(0)) == 1 || previousWorld(item(1))(item(0)) == 2) {
			youngNeighborsCounter += 1
		} else if (previousWorld(item(1))(item(0)) == 3 || previousWorld(item(1))(item(0)) == 4 || previousWorld(item(1))(item(0)) == 5) {
			adultNeighborsCounter += 1
		} else if (previousWorld(item(1))(item(0)) >= 6 ) {
			oldNeighborsCounter += 1
		})

		val totalLivingNeighbors = youngNeighborsCounter + adultNeighborsCounter + oldNeighborsCounter
		
		living match {
		case 0 => {
			if (totalLivingNeighbors == 3 && oldNeighborsCounter <= 1)
				living = 1
		}
		case 1 | 2 => {
			if (totalLivingNeighbors < 2 || totalLivingNeighbors > 3) living = 0
			else living += 1
		}
		case 3 | 4 | 5 => {
			if (totalLivingNeighbors < 2 || totalLivingNeighbors > 3) living = 0
			else living += 1
		}
		case _ => {
			if (totalLivingNeighbors != 3) living = 0
			else living += 1
		}
		}
	}
	
}