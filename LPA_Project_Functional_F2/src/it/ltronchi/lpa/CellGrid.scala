package it.ltronchi.lpa

import java.awt.Dimension
import scala.swing.Component
import scala.swing.GridPanel
import java.awt.Color

object CellGrid {
	def createCellsMatrix(rows:Int, columns:Int, previousWorld:Array[Array[Int]], init:Boolean, currentY:Int, currentX:Int):IndexedSeq[MyCell] = {
		if (currentX < 0 || currentY < 0) return IndexedSeq.empty
		else if (currentX == 0) createCellsMatrix(rows, columns, previousWorld, init, currentY -1, columns -1) :+ new MyCell(currentX, currentY, previousWorld, init)
		else createCellsMatrix(rows, columns, previousWorld, init, currentY, currentX -1) :+ new MyCell(currentX, currentY, previousWorld, init)		
	}
}


class CellGrid(override val rows:Int, override val columns:Int, val previousWorld:Array[Array[Int]], val init:Boolean) extends GridPanel(rows, columns) {

	MyCell.worldX = columns
	MyCell.worldY = rows
	
	contents appendAll _cells
	private lazy val _cells = CellGrid.createCellsMatrix(rows, columns, previousWorld, init, rows-1, columns-1) 
	
	enabled = false
	preferredSize = new Dimension(columns*20, rows*20)
	
	def this(rows:Int, columns:Int) = this(rows, columns,
			Array.tabulate[Int](Main.possibleWorldDimension(Main.currentWorld)(0),Main.possibleWorldDimension(Main.currentWorld)(1))((x, y) => 0), true)
	
	def cells = _cells
	
}

