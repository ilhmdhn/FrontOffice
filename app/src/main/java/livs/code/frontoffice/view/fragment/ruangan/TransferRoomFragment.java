package livs.code.frontoffice.view.fragment.ruangan;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.textfield.TextInputLayout;
import com.tuyenmonkey.mkloader.MKLoader;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import livs.code.frontoffice.MyApp;
import livs.code.frontoffice.R;
import livs.code.frontoffice.data.entity.Room;
import livs.code.frontoffice.data.entity.User;
import livs.code.frontoffice.data.remote.ApiRestService;
import livs.code.frontoffice.data.remote.MemberClient;
import livs.code.frontoffice.data.remote.RoomOrderClient;
import livs.code.frontoffice.data.remote.respons.RoomOrderResponse;
import livs.code.frontoffice.data.remote.respons.RoomResponse;
import livs.code.frontoffice.data.remote.respons.VoucherResponse;
import livs.code.frontoffice.events.EventsWrapper;
import livs.code.frontoffice.events.GlobalBus;
import livs.code.frontoffice.view.component.SearchRoomReadySpinnerDialog;
import livs.code.frontoffice.viewmodel.RoomViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TransferRoomFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TransferRoomFragment extends Fragment {

    @BindView(R.id.transferMinQmAnak)
    ImageView inputMinQmAnak;
    @BindView(R.id.transferPlusQmAnak)
    ImageView inputPlusQmAnak;
    @BindView(R.id.transferCountQmAnak)
    TextView inputCountQmAnak;
    int qmAnak = 0;

    @BindView(R.id.transferMinQmRemaja)
    ImageView inputMinQmRemaja;
    @BindView(R.id.transferPlusQmRemaja)
    ImageView inputPlusQmRemaja;
    @BindView(R.id.transferCountQmRemaja)
    TextView inputCountQmRemaja;
    int qmRemaja = 0;

    @BindView(R.id.transferMinQmDewasa)
    ImageView inputMinQmDewasa;
    @BindView(R.id.transferPlusQmDewasa)
    ImageView inputPlusQmDewasa;
    @BindView(R.id.transferCountQmDewasa)
    TextView inputCountQmDewasa;
    int qmDewasa = 0;

    @BindView(R.id.transferMinQmTua)
    ImageView inputMinQmTua;
    @BindView(R.id.transferPlusQmTua)
    ImageView inputPlusQmTua;
    @BindView(R.id.transferCountQmTua)
    TextView inputCountQmTua;
    int qmTua = 0;

    @BindView(R.id.transferMinQfAnak)
    ImageView inputMinQfAnak;
    @BindView(R.id.transferPlusQfAnak)
    ImageView inputPlusQfAnak;
    @BindView(R.id.transferCountQfAnak)
    TextView inputCountQfAnak;
    int qfAnak = 0;

    @BindView(R.id.transferMinQfRemaja)
    ImageView inputMinQfRemaja;
    @BindView(R.id.transferPlusQfRemaja)
    ImageView inputPlusQfRemaja;
    @BindView(R.id.transferCountQfRemaja)
    TextView inputCountQfRemaja;
    int qfRemaja = 0;

    @BindView(R.id.transferMinQfDewasa)
    ImageView inputMinQfDewasa;
    @BindView(R.id.transferPlusQfDewasa)
    ImageView inputPlusQfDewasa;
    @BindView(R.id.transferCountQfDewasa)
    TextView inputCountQfDewasa;
    int qfDewasa = 0;

    @BindView(R.id.transferMinQfTua)
    ImageView inputMinQfTua;
    @BindView(R.id.transferPlusQfTua)
    ImageView inputPlusQfTua;
    @BindView(R.id.transferCountQfTua)
    TextView inputCountQfTua;
    int qfTua = 0;

    @BindView(R.id.transferOverpackWarning)
    TextView overpackWarning;
    @BindView(R.id.transferInputVisitorName)
    TextInputLayout txtInputVisitorName;
    @BindView(R.id.transferInputVisitorPhone)
    TextInputLayout txtInputVisitorPhone;


    @BindView(R.id.transferOldRoom)
    TextView txtOldRoom;

    @BindView(R.id.transferBttnChooseRoom)
    AppCompatButton bttnChooseRoom;
    @BindView(R.id.transferChoosedRoom)
    TextView txtChoosedRoom;
    @BindView(R.id.transferInputDesc)
    TextInputLayout descTransfer;

    @BindView(R.id.transferButtonScanQrCodeVoucher)
    AppCompatButton buttonScanQRCodeVoucher;
    @BindView(R.id.transferCheckCodeVoucher)
    AppCompatButton buttonCheckCodeVoucher;
    @BindView(R.id.transferResetCodeVoucher)
    AppCompatButton buttonResetCodeVoucher;
    @BindView(R.id.transferInputCodeVoucher)
    TextInputLayout inputCodeVoucher;
    @BindView(R.id.view_output_cek_code_voucher)
    View viewOutputCekCodeVoucher;
    @BindView(R.id.img_output_cek_code_voucher)
    ImageView outputCheckImgVoucher;
    @BindView(R.id.txt_output_cek_code_voucher)
    TextView outputCheckTxtVoucher;
    private boolean isVoucherScanActive;
    private String finalVoucherCode = "";



    @BindView(R.id.bttn_transfer)
    AppCompatButton bttnTransfer;

    @BindView(R.id.transferProgressbar)
    MKLoader progressBar;

    private SearchRoomReadySpinnerDialog searchRoomReadySpinnerDialog;
    private RoomViewModel roomViewModel;
    private ArrayList<Room> roomArrayList = new ArrayList<>();


    private RoomOrderClient roomOrderClient;
    private MemberClient memberClient;


    private Room room, oldRoom, choosedRoom=null;
    private String EMPTY_ROOM_TRANSFER;
    private static String BASE_URL;
    private User USER_FO;


    public TransferRoomFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TransferRoomFragment.
     */

    public static TransferRoomFragment newInstance(String param1, String param2) {
        TransferRoomFragment fragment = new TransferRoomFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        room = TransferRoomFragmentArgs.fromBundle(getArguments()).getRoom();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer_room, container, false);
        ButterKnife.bind(this, view);
        BASE_URL = ((MyApp) getActivity().getApplicationContext()).getBaseUrl();
        USER_FO = ((MyApp) getActivity().getApplicationContext()).getUserFo();
        roomViewModel = new ViewModelProvider(getActivity())
                .get(RoomViewModel.class);
        roomViewModel.init(BASE_URL);

        memberClient = ApiRestService.getClient(BASE_URL).create(MemberClient.class);
        roomOrderClient = ApiRestService.getClient(BASE_URL).create(RoomOrderClient.class);



        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EMPTY_ROOM_TRANSFER = getResources().getString(R.string.pilih_room_transfer);

        setMainTitle();
        initOldRoom();
        //readyRoomSetupData();
        initQfUI();
        initQmUI();
        initVoucherValidationUI();
        bttnChooseRoom.setOnClickListener(view -> {
            if (txtChoosedRoom.getText().toString().equals(EMPTY_ROOM_TRANSFER)) {
                //chooseRoomReadyDialog();
                readyRoomSetupData();
            } else {
                bttnChooseRoom.setText("PILIH");
                choosedRoom = null;
                txtChoosedRoom.setText(EMPTY_ROOM_TRANSFER);
            }
        });
        bttnTransfer.setOnClickListener(view -> {
            submitData();
        });

    }

    private void setMainTitle() {
        GlobalBus
                .getBus()
                .post(new EventsWrapper
                        .TitleFragment(room.getRoomType()+" "+room.getRoomCode()));
    }

    private void submitData() {
        if(null==choosedRoom||!isInputValid(descTransfer.getEditText())){
            Toast
                    .makeText(getContext(), "Mohon Periksa Kembali", Toast.LENGTH_SHORT)
                    .show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);

        finalVoucherCode = inputCodeVoucher.getEditText().getText().toString();
        String keterangan = descTransfer.getEditText().getText().toString();
        Call<RoomOrderResponse> req = roomOrderClient
                .submitTransferRoom(
                        choosedRoom.getRoomCode(),
                        oldRoom.getRoomCode(),
                        keterangan,
                        finalVoucherCode,
                        USER_FO.getUserId(),
                        qmAnak,
                        qmRemaja,
                        qmDewasa,
                        qmTua,
                        qfAnak,
                        qfRemaja,
                        qfDewasa,
                        qfTua);

        req.enqueue(new Callback<RoomOrderResponse>() {
            @Override
            public void onResponse(Call<RoomOrderResponse> call, Response<RoomOrderResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomOrderResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                navToMain();
            }

            @Override
            public void onFailure(Call<RoomOrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
    private void navToMain() {
       /* Navigation
                .findNavController(this.getView())
                .navigate(TransferRoomFragmentDirections
                        .actionNavTransferRoomFragmentToNavListRoom());*/
    }

    private boolean isInputValid(EditText input) {
        String inputVal = input.getText().toString().trim();
        if (inputVal.isEmpty()) {

            //input.setError("Harap di isi");
            return false;
        } else {
            input.setError(null);
            return true;
        }
    }
    private void initOldRoom() {
        progressBar.setVisibility(View.VISIBLE);
        Call<RoomResponse> call = roomOrderClient.getRoomDetail(room.getRoomCode());
        call.enqueue(new Callback<RoomResponse>() {
            @Override
            public void onResponse(Call<RoomResponse> call, Response<RoomResponse> response) {
                progressBar.setVisibility(View.GONE);
                RoomResponse res = response.body();
                res.displayMessage(getContext());
                if (!res.isOkay()) {
                    return;
                }
                oldRoom = res.getRoom();
                initViewOldRoom();

            }

            @Override
            public void onFailure(Call<RoomResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void initViewOldRoom() {
        txtInputVisitorName.getEditText().setText(oldRoom.getRoomGuessName());
        txtInputVisitorName.setEnabled(false);
        txtInputVisitorPhone.getEditText().setText(oldRoom.getRoomGuessHp());
        if (!oldRoom.getVoucherCode().isEmpty()) {
            inputCodeVoucher.getEditText().setText(oldRoom.getVoucherCode());
            buttonResetCodeVoucher.setVisibility(View.VISIBLE);
        }
        txtOldRoom.setText("OLD ROOM : " + oldRoom.getRoomType() + " " + oldRoom.getRoomCode());

        inputCountQfAnak.setText(String.valueOf(oldRoom.getQf1()));
        inputCountQfRemaja.setText(String.valueOf(oldRoom.getQf2()));
        inputCountQfDewasa.setText(String.valueOf(oldRoom.getQf3()));
        inputCountQfTua.setText(String.valueOf(oldRoom.getQf4()));
        qfAnak = oldRoom.getQf1();
        qfRemaja = oldRoom.getQf2();
        qfDewasa = oldRoom.getQf3();
        qfTua = oldRoom.getQf4();

        inputCountQmAnak.setText(String.valueOf(oldRoom.getQm1()));
        inputCountQmRemaja.setText(String.valueOf(oldRoom.getQm2()));
        inputCountQmDewasa.setText(String.valueOf(oldRoom.getQm3()));
        inputCountQmTua.setText(String.valueOf(oldRoom.getQm4()));
        qmAnak = oldRoom.getQm1();
        qmRemaja = oldRoom.getQm2();
        qmDewasa = oldRoom.getQm3();
        qmTua = oldRoom.getQm4();

        countTotalVisitor();

    }

    private void readyRoomSetupData() {
        roomArrayList.clear();
        progressBar.setVisibility(View.VISIBLE);
        roomViewModel.getRoomReady().observe(getActivity(), roomResponse -> {
            roomResponse.displayMessage(getContext());
            if (roomResponse.isOkay()) {
                List<Room> listRoom = roomResponse.getRooms();
                roomArrayList.addAll(listRoom);
                chooseRoomReadyDialog();

            }

            progressBar.setVisibility(View.GONE);
        });
    }

    private void chooseRoomReadyDialog() {
      /*  if (roomArrayList.size() < 1) {
            readyRoomSetupData();
            return;
        }*/
        Toast
                .makeText(getContext(), "Data Room Ready Update", Toast.LENGTH_SHORT)
                .show();
        searchRoomReadySpinnerDialog = new SearchRoomReadySpinnerDialog(
                getActivity(), roomArrayList, "Pilih Room Transfer"
        );

        searchRoomReadySpinnerDialog.bindOnSpinerListener(new SearchRoomReadySpinnerDialog.OnSpinerItemClick() {
            @Override
            public void onClick(Room room) {
                choosedRoom = room;
                txtChoosedRoom.setText(choosedRoom.getRoomType() + " " + choosedRoom.getRoomCode());
                bttnChooseRoom.setText("Hapus");
            }
        });
        searchRoomReadySpinnerDialog.showSpinerDialog();
    }

    private void initVoucherValidationUI() {
        buttonScanQRCodeVoucher.setOnClickListener(view -> {
            if (!isVoucherScanActive) {
                try {
                    progressBar.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //integrator.initiateScan();
                            isVoucherScanActive = true;
                        }
                    }, 500);

                } catch (Exception e) {
                    Toast.makeText(getContext(), "Error Karena " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonResetCodeVoucher.setOnClickListener(view -> {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    buttonScanQRCodeVoucher.setVisibility(View.VISIBLE);
                    buttonResetCodeVoucher.setVisibility(View.GONE);
                    isVoucherScanActive = false;

                    inputCodeVoucher.setFocusable(false);
                    inputCodeVoucher.setEnabled(true);
                    inputCodeVoucher.getEditText().setText("");
                }
            }, 500);
        });

        buttonCheckCodeVoucher.setOnClickListener(view -> {
            String codeVcr = inputCodeVoucher.getEditText().getText().toString();
            if (codeVcr.equals("") || codeVcr == "") {
                Toast.makeText(getContext(),
                        "Kode Masih Kosong",
                        Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            progressBar.setVisibility(View.VISIBLE);

            Call<VoucherResponse> call = memberClient.checkVoucherMembership("HP059", codeVcr);
            call.enqueue(new Callback<VoucherResponse>() {
                @Override
                public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                    progressBar.setVisibility(View.GONE);
                    viewOutputCekCodeVoucher.setVisibility(View.VISIBLE);
                    VoucherResponse res = response.body();
                    if (!res.isOkay()) {
                        Toast
                                .makeText(getContext(),
                                        "Server Response : " + res.getMessage(),
                                        Toast.LENGTH_SHORT)
                                .show();
                        outputCheckImgVoucher.setImageResource(R.drawable.ic_unverified);
                        outputCheckTxtVoucher.setText("Code Invalid");
                        outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Toast
                                        .makeText(getContext(),
                                                "Memeriksa Voucher Lokal",
                                                Toast.LENGTH_SHORT)
                                        .show();
                                progressBar.setVisibility(View.VISIBLE);
                                checkLocalVoucher(codeVcr);
                            }
                        }, 100);

                        return;
                    }
                    finalVoucherCode = codeVcr;
                    outputCheckImgVoucher.setImageResource(R.drawable.ic_verified);
                    outputCheckTxtVoucher.setText("Valid");
                    outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(getContext(), R.color.green));
                    buttonResetCodeVoucher.setVisibility(View.VISIBLE);
                }

                @Override
                public void onFailure(Call<VoucherResponse> call, Throwable t) {
                    Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
        });
    }

    private void checkLocalVoucher(String codVcr) {
        Call<VoucherResponse> call = memberClient.checkVoucherOutlet(codVcr);
        call.enqueue(new Callback<VoucherResponse>() {
            @Override
            public void onResponse(Call<VoucherResponse> call, Response<VoucherResponse> response) {
                progressBar.setVisibility(View.GONE);
                viewOutputCekCodeVoucher.setVisibility(View.VISIBLE);
                VoucherResponse res = response.body();
                if (!res.isOkay()) {
                    Toast
                            .makeText(getContext(),
                                    " Server Response : " + res.getMessage(),
                                    Toast.LENGTH_SHORT)
                            .show();
                    outputCheckImgVoucher.setImageResource(R.drawable.ic_unverified);
                    outputCheckTxtVoucher.setText("Invalid");
                    outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
                    return;
                }

                finalVoucherCode = codVcr;
                outputCheckImgVoucher.setImageResource(R.drawable.ic_verified);
                outputCheckTxtVoucher.setText("Valid");
                outputCheckTxtVoucher.setTextColor(ContextCompat.getColor(getContext(), R.color.green));

            }

            @Override
            public void onFailure(Call<VoucherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "On Failure : " + t.toString(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void initQmUI() {
        inputMinQmAnak.setOnClickListener(view -> {
            if (qmAnak == 0) {
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }

            if (qmAnak > 0) {

                qmAnak = qmAnak - 1;
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }
            countTotalVisitor();
        });
        inputPlusQmAnak.setOnClickListener(view -> {
            if (qmAnak == 10) {
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }

            if (qmAnak < 10) {
                qmAnak = qmAnak + 1;
                inputCountQmAnak.setText(String.valueOf(qmAnak));
            }
            countTotalVisitor();
        });
        inputMinQmRemaja.setOnClickListener(view -> {
            if (qmRemaja == 0) {
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }

            if (qmRemaja > 0) {

                qmRemaja = qmRemaja - 1;
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }
            countTotalVisitor();
        });
        inputPlusQmRemaja.setOnClickListener(view -> {
            if (qmRemaja == 10) {
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }

            if (qmRemaja < 10) {
                qmRemaja = qmRemaja + 1;
                inputCountQmRemaja.setText(String.valueOf(qmRemaja));
            }
            countTotalVisitor();
        });
        inputMinQmDewasa.setOnClickListener(view -> {
            if (qmDewasa == 0) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }

            if (qmDewasa > 0) {

                qmDewasa = qmDewasa - 1;
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }
            countTotalVisitor();
        });
        inputPlusQmDewasa.setOnClickListener(view -> {
            if (qmDewasa == 10) {
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }

            if (qmDewasa < 10) {
                qmDewasa = qmDewasa + 1;
                inputCountQmDewasa.setText(String.valueOf(qmDewasa));
            }
            countTotalVisitor();
        });
        inputMinQmTua.setOnClickListener(view -> {
            if (qmTua == 0) {
                inputCountQmTua.setText(String.valueOf(qmTua));
            }

            if (qmTua > 0) {

                qmTua = qmTua - 1;
                inputCountQmTua.setText(String.valueOf(qmTua));
            }
            countTotalVisitor();
        });
        inputPlusQmTua.setOnClickListener(view -> {
            if (qmTua == 10) {
                inputCountQmTua.setText(String.valueOf(qmTua));
            }

            if (qmTua < 10) {
                qmTua = qmTua + 1;
                inputCountQmTua.setText(String.valueOf(qmTua));
            }
            countTotalVisitor();
        });
    }

    private void initQfUI() {
        inputMinQfAnak.setOnClickListener(view -> {
            if (qfAnak == 0) {
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }

            if (qfAnak > 0) {

                qfAnak = qfAnak - 1;
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }
            countTotalVisitor();
        });
        inputPlusQfAnak.setOnClickListener(view -> {
            if (qfAnak == 10) {
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }

            if (qfAnak < 10) {
                qfAnak = qfAnak + 1;
                inputCountQfAnak.setText(String.valueOf(qfAnak));
            }
            countTotalVisitor();
        });
        inputMinQfRemaja.setOnClickListener(view -> {
            if (qfRemaja == 0) {
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }

            if (qfRemaja > 0) {

                qfRemaja = qfRemaja - 1;
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }
            countTotalVisitor();
        });
        inputPlusQfRemaja.setOnClickListener(view -> {
            if (qfRemaja == 10) {
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }

            if (qfRemaja < 10) {
                qfRemaja = qfRemaja + 1;
                inputCountQfRemaja.setText(String.valueOf(qfRemaja));
            }
            countTotalVisitor();
        });
        inputMinQfDewasa.setOnClickListener(view -> {
            if (qfDewasa == 0) {
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }

            if (qfDewasa > 0) {

                qfDewasa = qfDewasa - 1;
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }
            countTotalVisitor();
        });
        inputPlusQfDewasa.setOnClickListener(view -> {
            if (qfDewasa == 10) {
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }

            if (qfDewasa < 10) {
                qfDewasa = qfDewasa + 1;
                inputCountQfDewasa.setText(String.valueOf(qfDewasa));
            }
            countTotalVisitor();
        });
        inputMinQfTua.setOnClickListener(view -> {
            if (qfTua == 0) {
                inputCountQfTua.setText(String.valueOf(qfTua));
            }

            if (qfTua > 0) {

                qfTua = qfTua - 1;
                inputCountQfTua.setText(String.valueOf(qfTua));
            }
            countTotalVisitor();
        });
        inputPlusQfTua.setOnClickListener(view -> {
            if (qfTua == 10) {
                inputCountQfTua.setText(String.valueOf(qfTua));
            }

            if (qfTua < 10) {
                qfTua = qfTua + 1;
                inputCountQfTua.setText(String.valueOf(qfTua));
            }
            countTotalVisitor();
        });
    }

    private void countTotalVisitor() {
        int total = qmAnak + qmDewasa + qmRemaja + qmTua + qfAnak + qfDewasa + qfRemaja + qfTua;
        if (total > room.getRoomCapacity()) {
            int over = total - room.getRoomCapacity();
            overpackWarning.setVisibility(View.VISIBLE);
            overpackWarning.setText(" Overpack " + String.valueOf(over));
        } else {
            overpackWarning.setVisibility(View.GONE);
            overpackWarning.setText("");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!GlobalBus.getBus().isRegistered(this))
            EventBus.getDefault().register(this);

    }

    @Subscribe
    public void setDataQrCode(EventsWrapper.ScanResult scanResult) {
        progressBar.setVisibility(View.GONE);
        if (!scanResult.isSuccess()) {
            isVoucherScanActive = false;
            return;
        }
        if (isVoucherScanActive) {
            if(scanResult.getData()!=null){
                buttonScanQRCodeVoucher.setVisibility(View.GONE);
                buttonResetCodeVoucher.setVisibility(View.VISIBLE);

                inputCodeVoucher.setFocusable(true);
                inputCodeVoucher.setEnabled(false);
                inputCodeVoucher.getEditText().setText(scanResult.getData());
            }

            isVoucherScanActive = false;
        }

    }

}