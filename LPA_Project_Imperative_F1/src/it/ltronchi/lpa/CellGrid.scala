package it.ltronchi.lpa

import java.awt.Dimension
import scala.swing.Component
import scala.swing.GridPanel
import java.awt.Color

//class CellGrid(val row:Int, val column:Int) extends GridPanel(row, column) {
//
//	
////	ArrayBuffer.
//	
//	contents ++= ArrayBuffer.fill(row*column){new Cell(0,0,Color.BLACK)}
////	contents ++= Array.fromFunction((x:Int,y:Int)=>{_.to})//Buffer.fill(row*column){new Cell(Color.BLACK)}
//
////	var matrix: Array[Array[Cell]] = new 
////	this.
//
//}

class CellGrid(override val rows:Int, override val columns:Int) extends GridPanel(rows, columns) {

	MyCell.worldX = columns
	MyCell.worldY = rows
	
	contents appendAll cellsSeq
	private lazy val cellsSeq = for {
		y <- 0 until rows
		x <- 0 until columns
	} yield new MyCell(x, y)

	enabled = false
	preferredSize = new Dimension(columns*20, rows*20)

	def updateAll(previousWorld: Array[Array[Boolean]]) {
		contents foreach (element => element match {
		case cell: MyCell => cell.updateState(previousWorld)
		})
	}
	
	def cells = cellsSeq
	
}

