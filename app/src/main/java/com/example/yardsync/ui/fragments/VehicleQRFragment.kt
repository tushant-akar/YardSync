package com.example.yardsync.ui.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.yardsync.R
import com.example.yardsync.databinding.FragmentVehicleQRBinding
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.QrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorBallShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorFrameShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorPixelShape
import com.github.alexzhirkevich.customqrgenerator.vector.style.QrVectorShapes
import java.io.FileOutputStream

class VehicleQRFragment : Fragment() {
    private var _binding: FragmentVehicleQRBinding? = null
    private val binding get() = _binding!!
    private val args: VehicleQRFragmentArgs by navArgs()
    private lateinit var vehicleNumber: String
    private lateinit var parkingLot: String
    private var dockNo: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehicleQRBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vehicleNumber = args.vehicleNumber
        parkingLot = args.ParkingLot
        dockNo = args.DockNo

        binding.qrCode.setImageDrawable(generateQRCode())

        binding.parkingLot.text = "Assigned Parking Lot: $parkingLot"
        binding.dockNo.text = "Assigned Dock No: $dockNo"

        binding.homeBtn.setOnClickListener {
            findNavController().navigate(R.id.action_vehicleQRFragment_to_mainActivity)
        }

        binding.printBtn.setOnClickListener {
            val bitmap = captureFragmentView(this)
            printFragment(requireContext(), bitmap, "Vehicle QR Code")
        }
    }

    private fun captureFragmentView(fragment: Fragment): Bitmap {
        val view = fragment.requireView()
        view.isDrawingCacheEnabled = true
        view.buildDrawingCache()
        val bitmap = Bitmap.createBitmap(view.drawingCache)
        view.isDrawingCacheEnabled = false
        return bitmap
    }

    private fun printFragment(context: Context, bitmap: Bitmap, jobName: String) {
        val printManager = context.getSystemService(Context.PRINT_SERVICE) as PrintManager

        printManager.print(jobName, object : PrintDocumentAdapter() {

            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: CancellationSignal?,
                callback: LayoutResultCallback?,
                extras: Bundle?
            ) {
                val info = PrintDocumentInfo.Builder(jobName)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                    .build()
                callback?.onLayoutFinished(info, true)
            }

            override fun onWrite(
                pages: Array<PageRange>,
                destination: ParcelFileDescriptor,
                cancellationSignal: CancellationSignal,
                callback: WriteResultCallback
            ) {
                try {
                    val outputStream = FileOutputStream(destination.fileDescriptor)
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    outputStream.close()
                    callback.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                } catch (e: Exception) {
                    callback.onWriteFailed(e.toString())
                }
            }
        }, null)
    }



    private fun generateQRCode(): Drawable {
        val data = QrData.Text(vehicleNumber)

        val options = QrVectorOptions.Builder()
            .setPadding(.3f)
            .setShapes(
                QrVectorShapes(
                    darkPixel = QrVectorPixelShape.RoundCorners(.5f),
                    ball = QrVectorBallShape.RoundCorners(.25f),
                    frame = QrVectorFrameShape.RoundCorners(.25f)
                )
            )
            .build()

        val drawable = QrCodeDrawable(data, options)

        return drawable
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}