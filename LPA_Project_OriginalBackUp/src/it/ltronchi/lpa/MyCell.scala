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
	
	
	size = new Dimension(10,10)
	contents += new Label("")
	
	border = LineBorder.createGrayLineBorder
	
	var living = false
	background = Color.WHITE
	
	listenTo(mouse.clicks) 
	reactions += {
		case e: MouseClicked =>{
			if (living) {
				living = false
				background = Color.WHITE
			}
			else {
				living = true 
				background = Color.BLACK
			}
			repaint()
		}
	}
	
	def isAlive():Boolean = {
		living
	}
	
	def setAlive(alive:Boolean) {
		living = alive
		if (alive == true) {
			background = Color.BLACK
		} else {
			background = Color.WHITE
		}	
	}
	
	def updateState(previousWorld: Array[Array[Boolean]]) {
		val neighbors = getNeighbors()
		var aliveNeighborsCounter = 0;
		
		neighbors foreach(item => if (previousWorld(item(1))(item(0))) aliveNeighborsCounter += 1 )
//		if (living) println(aliveNeighborsCounter)
		if (living && aliveNeighborsCounter < 2) setAlive(false)
		if (living && aliveNeighborsCounter > 3) setAlive(false)
		if (!living && aliveNeighborsCounter == 3) setAlive(true) 
	}
	
	def getNeighbors() :Set[Array[Int]] = {
		
		var neighbors = Set.empty[Array[Int]]
		for (i <- -1 to 1) {
			for (j <- -1 to 1) {
				if (!(i==0 && j==0))
					neighbors += Array((x + MyCell.worldX + i) % MyCell.worldX, (y + MyCell.worldY + j) % MyCell.worldY)
			}
		}		
		return neighbors
		
//		if (x == 10 && y == 10) {
//			neighbors.foreach(i => print("(" + i.deep.mkString(",") + ")") + ",")
//			println
//		}
	}
	
//	public Vector<Cell> neighbour8Cells(Cell myCell) {
//		//System.out.println("------------------["+myCell.getRow()+"]["+myCell.getCol()+"]------------------");
//		int row = myCell.getRow();
//		int col = myCell.getCol();
//		Vector<Cell> neighbours = new Vector<Cell>();
//		int initI = Math.max(0, row - 1);
//		int initJ = Math.max(0, col - 1);
//		for (int i = initI; i < initI+Math.min(3+(row-1<0?row-1:0), dim - row + 1); i++) {
//			for(int j = initJ; j < initJ+Math.min(3+(col-1<0?col-1:0), dim - col + 1); j++) {
//				//System.out.print("["+i+"]["+j+"] ");
//				if(!(i == row && j == col))
//					neighbours.add(matrix[i][j]);
//			}
//			//System.out.println();
//		}
//		//System.out.println("------------------------------------");
//		return neighbours;
//	}
	
//	override def paintComponent(g:Graphics2D) = {
//		super.paintComponent(g)
//	}
		
}