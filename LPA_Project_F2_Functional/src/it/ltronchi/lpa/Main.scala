package it.ltronchi.lpa


import scala.collection.mutable.Set
import scala.swing.SimpleSwingApplication
import scala.swing.MainFrame
import scala.swing.BorderPanel
import BorderPanel.Position._
import scala.swing.Button
import java.awt.Dimension
import java.awt.Color
import scala.swing.ScrollPane
import scala.swing.FlowPanel
import scala.swing.Component
import scala.collection.mutable.ArrayBuffer
import scala.swing.event.ButtonClicked
import scala.swing.ComboBox
import scala.swing.Slider
import scala.swing.event.ValueChanged
import scala.swing.Label
import scala.swing.ComboBox
import scala.swing.event.SelectionChanged
import scala.swing.event.SelectionChanged

object Main extends SimpleSwingApplication {
	
	val storedConfiguration = Array(
			Set(Array(24,2),Array(25,2),Array(24,3),Array(26,3),Array(12,4),Array(25,4),Array(26,4),Array(27,4),Array(9,5),
					Array(10,5),Array(11,5),Array(12,5),Array(16,5),Array(17,5),Array(26,5),Array(27,5),Array(28,5),Array(35,5),Array(36,5),
					Array(8,6),Array(9,6),Array(10,6),Array(11,6),Array(15,6),Array(16,6),Array(25,6),Array(26,6),Array(27,6),Array(35,6),
					Array(36,6),Array(1,7),Array(2,7),Array(8,7),Array(11,7),Array(15,7),Array(16,7),Array(18,7),Array(20,7),Array(21,7),
					Array(24,7),Array(26,7),Array(1,8),Array(2,8),Array(8,8),Array(9,8),Array(10,8),Array(11,8),Array(16,8),Array(18,8),
					Array(20,8),Array(21,8),Array(24,8),Array(25,8),Array(9,9),Array(10,9),Array(11,9),Array(12,9),Array(16,9),Array(18,9),
					Array(19,9),Array(12,10)),
			Set(Array(28,6),Array(29,6),Array(39,6),Array(40,6),Array(27,7),Array(29,7),Array(39,7),Array(40,7),Array(5,8),Array(6,8),
					Array(14,8),Array(15,8),Array(27,8),Array(28,8),Array(5,9),Array(6,9),Array(13,9),Array(15,9),Array(13,10),Array(14,10),
					Array(21,10),Array(22,10),Array(21,11),Array(23,11),Array(21,12),Array(40,13),Array(41,13),Array(40,14),Array(42,14),
					Array(40,15),Array(29,18),Array(30,18),Array(31,18),Array(29,19),Array(30,20)),
			Set(Array(5,7),Array(6,7),Array(7,7),Array(8,7),Array(9,7),Array(10,7),Array(11,7),Array(12,7),Array(13,7),Array(14,7)),
			Set(Array(8,6),Array(9,6),Array(7,7),Array(8,7),Array(8,8)),
			Set[Array[Int]]()
	)
	val storedConfigurationMapDim = Array("Big", "Big", "Small", "Small", "Big")
	val confComboBox = new ComboBox(Seq("Gosper Glider Gun", "Ships", "Ten", "Pentonimo", "Empty"))
	

	val worldDimCombo = new ComboBox(Seq("Big", "Medium", "Small"))
	val possibleWorldDimension = Array(Array(30,45),Array(20,30),Array(15,20))
	var currentWorld = 0

	var previousWorld = Array.tabulate[Int](possibleWorldDimension(0)(0),possibleWorldDimension(0)(1))((x, y) => 0)
	
	var _speedCoeff = 1.0
	val sleepTime = 1.0//secondi
	

	var grid = new CellGrid(possibleWorldDimension(0)(0),possibleWorldDimension(0)(1))
	val pane = new ScrollPane(grid)
	
	def top = new MainFrame {
			
		contents = new BorderPanel {
			
			resizable = false
			layout(pane) = Center;
			
			val controlPanel = new FlowPanel {
				val loadConf = new Button("Load")
				val nextButton = new Button("Next")
				val playButton = new Button("Play/Pause")
				val speedSlider = new Slider
				speedSlider.min = 1
				speedSlider.max = 10
				speedSlider.value = 5
				

				listenTo(nextButton)
				listenTo(loadConf)
				listenTo(playButton)
				listenTo(speedSlider)
				listenTo(worldDimCombo.selection)
				
				reactions += {
				case ButtonClicked(`nextButton`) =>{next}
				case ButtonClicked(`loadConf`) =>{AutomaticEsecutor ! "a";loadConfiguration}
				case ButtonClicked(`playButton`) =>{AutomaticEsecutor ! "p"}
				case ValueChanged(`speedSlider`) =>{speedCoef = speedSlider.value}
				case SelectionChanged(`worldDimCombo`) =>{AutomaticEsecutor ! "a";changeMapDimension}
				}
	
				contents += worldDimCombo
				contents += confComboBox
				contents += loadConf
				contents += nextButton
				contents += playButton
				contents += speedSlider
			}
			
			layout(controlPanel) = South;
			AutomaticEsecutor.start()
		}
		
	}
	
	def saveWorld() = {
		grid.contents foreach (element => element match {
		case cell: MyCell => previousWorld(cell.y)(cell.x) = cell.living
		})

	}
	
	def next() = {
		grid = new CellGrid(possibleWorldDimension(currentWorld)(0), possibleWorldDimension(currentWorld)(1), previousWorld, false)
		saveWorld
		if (deepCheck(previousWorld, previousWorld.length - 1) == 0) AutomaticEsecutor ! "a"
		pane.contents = grid
	}
	
	def deepCheck(matrix:Any, idx:Int):Int = {
		matrix match {
		case value:Array[Int] =>	if (idx == 0) value(idx)
										else value(idx) + deepCheck(value, idx -1)
		case value:Array[Array[Int]] =>	if (idx == 0) deepCheck(value(idx), value(idx).length -1)
											else deepCheck(value(idx), value(idx).length -1) + deepCheck(value, idx -1)
		}
	}
	
	def loadConfiguration() = {
		
		worldDimCombo.selection.item = storedConfigurationMapDim(confComboBox.selection.index);
		changeMapDimension();		
		
		val selectedConf = storedConfiguration(confComboBox.selection.index)
		
		selectedConf.foreach(item => previousWorld(item(1))(item(0)) = 1)
		grid = new CellGrid(possibleWorldDimension(currentWorld)(0), possibleWorldDimension(currentWorld)(1), previousWorld, true)
		pane.contents = grid
		
	}
	
	def changeMapDimension() {
		worldDimCombo.selection.item match {
		case "Big" => {
			currentWorld = 0
			previousWorld = Array.tabulate[Int](possibleWorldDimension(0)(0),possibleWorldDimension(0)(1))((x, y) => 0)
			grid = new CellGrid(possibleWorldDimension(0)(0),possibleWorldDimension(0)(1))
		}
		case "Medium" => {
			currentWorld = 1
			previousWorld = Array.tabulate[Int](possibleWorldDimension(1)(0),possibleWorldDimension(1)(1))((x, y) => 0)
			grid = new CellGrid(possibleWorldDimension(1)(0),possibleWorldDimension(1)(1))
		}
		case "Small" => {
			currentWorld = 2
			previousWorld = Array.tabulate[Int](possibleWorldDimension(2)(0),possibleWorldDimension(2)(1))((x, y) => 0)
			grid = new CellGrid(possibleWorldDimension(2)(0),possibleWorldDimension(2)(1))
		}
		}
		pane.contents = grid
		
	}
	
	def changeCellStatus(x:Int, y:Int, previousStatus:Int) = {
		saveWorld()
		previousWorld(y)(x) = previousStatus match {
		case 0 => 1
		case 1|2 => 3
		case 3|4|5 => 6
		case _ => 0
		}
		grid = new CellGrid(possibleWorldDimension(currentWorld)(0), possibleWorldDimension(currentWorld)(1), previousWorld, true)
		pane.contents = grid
	}

	def speedCoef_= (value:Int) = _speedCoeff = value
	def speedCoef = _speedCoeff
}