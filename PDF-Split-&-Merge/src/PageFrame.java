import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

public class PageFrame extends JPanel{
	private static final long serialVersionUID = 1L;
	private Canvas imagePreview;
	private int positionNumber;
	private int width;
	private int height;
	private BeautyButton leftRotationButton;
	private BeautyButton rightRotationButton;
	private BeautyButton upPositionButton;
	private BeautyButton downPositionButton;
	private BeautyButton previewButton;
	private BeautyButton saveButton;
	private BeautyButton deleteButton;
	private PDPage page;
	private BufferedImage image;
	private Application app;
	private JTextField fieldPageNumber;
	private JTextField fieldDegreeOfRotation;
	private int rotation;
	private String lastPath;

	public PageFrame(int positionNumber, PDPage page, Application app) {
		rotation = 0;
		this.app = app;
		width = 597;
		height = 200;
		this.positionNumber = positionNumber;
		this.page = page;
		this.image = getBufferedImage();
		setLayout(null);
		setBackground(new Color(0xf2f2f2));
		addMouseListener(new PageFrameMouseListener());
		init();
	}
	
	private BufferedImage getBufferedImage() {
		BufferedImage image = null;
		try {
			image = page.convertToImage(BufferedImage.TYPE_INT_RGB, 16);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	private void init() {
		imagePreview = new Canvas();
		imagePreview.setBackground(new Color(0xf2f2f2));
		imagePreview.addDrawObject(new drawing.Image((180 - image.getWidth()) / 2, (180 - image.getHeight()) / 2, image));
		imagePreview.setBounds(25, 10, 180, 180);
		imagePreview.addMouseListener(new CanvasMouseListener());
		imagePreview.addMouseListener(new PageFrameMouseListener());
		
//		Поле номер страницы
		JLabel lablePageNumber = new JLabel("№ стр.: ");
		lablePageNumber.setBounds(250, 25, 70, 20);
		
		fieldPageNumber = new JTextField();
		fieldPageNumber.setBounds(300, 25, 30, 20);
		fieldPageNumber.setText(Integer.toString(positionNumber));
		fieldPageNumber.setHorizontalAlignment(JTextField.CENTER);
		fieldPageNumber.setEditable(false);
		fieldPageNumber.addMouseListener(new PageFrameMouseListener());
		
		upPositionButton = new BeautyButton("/arrow-up.png", "/arrow-up_rollover.png", "Переместить вверх");	
		upPositionButton.setBounds(this.width - 40, 46, 16, 64);
		upPositionButton.setVisible(false);
		upPositionButton.addActionListener(new UpPositionButtonListener());
		upPositionButton.addMouseListener(new PageFrameMouseListener());
		
		downPositionButton = new BeautyButton("/arrow-down.png", "/arrow-down_rollover.png", "Переместить вниз");
		downPositionButton.setBounds(this.width - 40, 126, 16, 64);
		downPositionButton.setVisible(false);
		downPositionButton.addActionListener(new DownPositionButtonListener());
		downPositionButton.addMouseListener(new PageFrameMouseListener());
		
//		Поля поворота по и против часовой
		JLabel lableDegreeOfRotation = new JLabel("Поворот: ");
		lableDegreeOfRotation.setBounds(240, 55, 100, 20);
		
		fieldDegreeOfRotation = new JTextField();
		fieldDegreeOfRotation.setBounds(300, 55, 30, 20);
		fieldDegreeOfRotation.setText("0");
		fieldDegreeOfRotation.setHorizontalAlignment(JTextField.CENTER);
		fieldDegreeOfRotation.setEditable(false);
		fieldDegreeOfRotation.addMouseListener(new PageFrameMouseListener());
		
		leftRotationButton = new BeautyButton("/spinner_left.png", "/spinner_left_rollover.png", "Повернуть против часовой");
		leftRotationButton.setBounds(335, 55, 20, 20);
		leftRotationButton.addActionListener(new LeftRotationButtonListener());
		leftRotationButton.addMouseListener(new PageFrameMouseListener());

		rightRotationButton = new BeautyButton("/spinner_right.png", "/spinner_right_rollover.png", "Повернуть по часовой");
		rightRotationButton.setBounds(355, 55, 20, 20);
		rightRotationButton.addActionListener(new RightRotationButtonListener());
		rightRotationButton.addMouseListener(new PageFrameMouseListener());
		
//		Предпросмотр странички
		previewButton = new BeautyButton("/search_24x24.png", "/search_rollover_24x24.png", "Посмотреть");
		previewButton.setBounds(180, 25, 24, 24);
		previewButton.setVisible(false);
		previewButton.addActionListener(new PreviewButtonListener());
		previewButton.addMouseListener(new PageFrameMouseListener());
		previewButton.addMouseListener(new CanvasMouseListener());
		
		saveButton = new BeautyButton("/save_button_24x24.png", "/save_button_rollover_24x24.png", "Сохранить страничку в файл");
		saveButton.setBounds(this.width - 100, 5, 24, 24);
		saveButton.setVisible(false);
		saveButton.addActionListener(new SaveButtonListener());
		saveButton.addMouseListener(new PageFrameMouseListener());
		
		deleteButton = new BeautyButton("/button_delete_red_24x24.png", "/button_delete_red_rollover_24x24.png", "Удалить");
		deleteButton.setBounds(this.width - 65, 5, 24, 24);
		deleteButton.setVisible(false);
		deleteButton.addActionListener(new DeleteButtonListener());
		deleteButton.addMouseListener(new PageFrameMouseListener());
		
//		Добавление элементов на PageFrame
		add(lablePageNumber);
		add(fieldPageNumber);
		add(upPositionButton);
		add(leftRotationButton);
		add(lableDegreeOfRotation);
		add(fieldDegreeOfRotation);
		add(downPositionButton);
		add(rightRotationButton);
		add(previewButton);
		add(saveButton);
		add(deleteButton);
		add(imagePreview);
	}
	
	public void resizeInterface() {
		upPositionButton.setBounds(this.width - 35, 46, 16, 64);
		downPositionButton.setBounds(this.width - 35, 126, 16, 64);
		previewButton.setBounds(180, 25, 24, 24);
		saveButton.setBounds(this.width - 100, 5, 24, 24);
		deleteButton.setBounds(this.width - 65, 5, 24, 24);
	}
	
	public PDPage getPage() {
		PDPage page = this.page;
		return page;
	}
	
	public int getPositionNumber() {
		return this.positionNumber;
	}
	
	public int getWidth() {
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	public void setPositionNumber(int positionNumber) {
		this.positionNumber = positionNumber;
	}
	
	private class UpPositionButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			upPositionButton.setSelected(false);
			if (positionNumber > 1) {
				app.swapPages(positionNumber, positionNumber - 1);
				PageFrame.this.getLocationOnScreen();
//				app.moveViewportSPane(-206);
			}
		}	
	}
	
	private class DownPositionButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			downPositionButton.setSelected(false);
			if (positionNumber < app.getNumberPageFrames()) {
				app.swapPages(positionNumber, positionNumber + 1);
//				app.moveViewportSPane(206);
			}
		}
		
	}
	
	private class LeftRotationButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			leftRotationButton.setSelected(false);
			rotation -= 90;
			if (rotation == -360) rotation = 0;
			page.setRotation(rotation);
			fieldDegreeOfRotation.setText(Integer.toString(rotation));
			image = getBufferedImage();
			PageFrame.this.remove(imagePreview);
			imagePreview = new Canvas();
			imagePreview.setBackground(new Color(0xf2f2f2));
			imagePreview.addDrawObject(new drawing.Image((180 - image.getWidth()) / 2, (180 - image.getHeight()) / 2, image));
			imagePreview.setBounds(25, 10, 180, 180);
			imagePreview.addMouseListener(new CanvasMouseListener());
			imagePreview.addMouseListener(new PageFrameMouseListener());
			PageFrame.this.add(imagePreview);
			app.repaint();
		}
		
	}
	
	private class RightRotationButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			rightRotationButton.setSelected(false);
			rotation += 90;
			if (rotation == 360) rotation = 0;
			page.setRotation(rotation);
			fieldDegreeOfRotation.setText(Integer.toString(rotation));
			image = getBufferedImage();
			PageFrame.this.remove(imagePreview);
			imagePreview = new Canvas();
			imagePreview.setBackground(new Color(0xf2f2f2));
			imagePreview.addDrawObject(new drawing.Image((180 - image.getWidth()) / 2, (180 - image.getHeight()) / 2, image));
			imagePreview.setBounds(25, 10, 180, 180);
			imagePreview.addMouseListener(new CanvasMouseListener());
			imagePreview.addMouseListener(new PageFrameMouseListener());
			PageFrame.this.add(imagePreview);
			app.repaint();
		}
		
	}
	
	private class PreviewButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			previewButton.setSelected(false);
			
			@SuppressWarnings("unused")
			PDFViewer viewer = new PDFViewer(getPage());
		}
		
	}
	
	private class SaveButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			saveButton.setSelected(false);
			
			JFileChooser fc;
			if (lastPath == null) {
				fc = new JFileChooser();
			} else {
				fc = new JFileChooser(lastPath);
			}
			
			int returnVal = fc.showSaveDialog(saveButton);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				PDDocument document;
				try {
					document = new PDDocument();
					document.addPage(getPage());
					
					String[] splitedPath = file.getPath().split("\\.");
					if (splitedPath[splitedPath.length - 1].equals("pdf")) {
						document.save(file.getPath());
					} else {
						document.save(file.getPath() + ".pdf");	
					}
				
					lastPath = file.getParent();
					document.close();

				} catch (IOException ex) {
					ex.printStackTrace();
				} catch (COSVisitorException ex1) {
					ex1.printStackTrace();
				} 
			}
		}
		
	}
	
	private class DeleteButtonListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			deleteButton.setSelected(false);
			app.deletePage(getPositionNumber() - 1);
			app.resizeInterface();
		}
		
	}
	
	private class PageFrameMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			upPositionButton.setVisible(true);
			downPositionButton.setVisible(true);
//			previewButton.setVisible(true);
			saveButton.setVisible(true);
			deleteButton.setVisible(true);
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			upPositionButton.setVisible(false);
			downPositionButton.setVisible(false);
//			previewButton.setVisible(false);
			saveButton.setVisible(false);
			deleteButton.setVisible(false);
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class CanvasMouseListener implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			previewButton.setVisible(true);
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			previewButton.setVisible(false);
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			@SuppressWarnings("unused")
			PDFViewer viewer = new PDFViewer(getPage());
			
		}
		
	}
	
	
}
