package so.contacts.hub.basefunction.widget;

import com.putao.live.R;

import so.contacts.hub.basefunction.widget.adapter.BaseListViewAdapter;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * ListView下拉刷新和加载更多<p>
 * 
 * <strong>变更说明:</strong>
 * <p>默认如果设置了OnRefreshListener接口和OnLoadMoreListener接口，<br>并且不为null，则打开这两个功能了。
 * <p>剩余两个Flag：mIsAutoLoadMore(是否自动加载更多)和
 * <br>mIsMoveToFirstItemAfterRefresh(下拉刷新后是否显示第一条Item)
 */
public class CustomListView extends ListView implements OnScrollListener {

	/**  显示格式化日期模板   */
	private final static String DATE_FORMAT_STR = "yyyy年MM月dd日 HH:mm";
	
	/**  实际的padding的距离与界面上偏移距离的比例   */
	private final static int RATIO = 3;
	
	private final static int RELEASE_TO_REFRESH = 0;
	private final static int PULL_TO_REFRESH = 1;
	private final static int REFRESHING = 2;
	private final static int DONE = 3;
	private final static int LOADING = 4;
	
	/**  加载中   */
	private final static int ENDINT_LOADING = 1;
	/**  手动完成刷新   */
	private final static int ENDINT_MANUAL_LOAD_DONE = 2;
	/**  自动完成刷新   */
	private final static int ENDINT_AUTO_LOAD_DONE = 3;
	

	/** 网络搜索中，请稍候...  add by zjh 2014-12-09*/
	private final static int ENDINT_LOADING_AND_WAIT = 4;

	private final static int ENDINT_LOAD_NO_MORE = 5;

	private static final String TAG = "CustomListView";
	
	/**    0:RELEASE_TO_REFRESH;
	 * <p> 1:PULL_To_REFRESH;
	 * <p> 2:REFRESHING;
	 * <p> 3:DONE;
	 * <p> 4:LOADING */
	private int mHeadState;
	/**    0:完成/等待刷新 ;
	 * <p> 1:加载中  */
	private int mEndState;
	
	// ================================= 功能设置Flag ================================
	
	/**  可以加载更多？   */
	private boolean mCanLoadMore = false;
	/**  可以下拉刷新？   */
	private boolean mCanRefresh = false;
	/**  可以自动加载更多吗？（注意，先判断是否有加载更多，如果没有，这个flag也没有意义）   */
	private boolean mIsAutoLoadMore = true;
	/** 下拉刷新后是否显示第一条Item    */
	private boolean mIsMoveToFirstItemAfterRefresh = false;
	/** 当前滚动状态 */
	private int mScrollState = 0; /** add by cj 2015/03/25 */
	
	private BaseListViewAdapter mAdapter;
	
	private int mTouchDownY; // 手指按下时的Y值,仅用于判断是上拉还是下拉
	
	private int mTouchUpY; //手指抬起时的Y值,仅用于判断是上拉还是下拉

    private boolean upFlag; // 判断是否手指抬起
	/*
     * modify by putao_lhq at 2015年1月9日 @start
     * add code
     */
	private String mTipString = null;
	private String mTipDoingString = null;
	
	public void setTipString(String tipStr) {
	    mTipString = tipStr;
	}
	
	public void setTipDoingString(String tipDoingStr) {
	    mTipDoingString = tipDoingStr;
	}
	/* end by putao_lhq */
	
	public boolean isCanLoadMore() {
		return mCanLoadMore;
	}
	
	public void setCanLoadMore(boolean pCanLoadMore) {
		mCanLoadMore = pCanLoadMore;
		if(mCanLoadMore && getFooterViewsCount() == 0){
			addFooterView();
		}
	}
	
	public boolean isCanRefresh() {
		return mCanRefresh;
	}
	
	public void setCanRefresh(boolean pCanRefresh) {
		mCanRefresh = pCanRefresh;
	}
	
	public boolean isAutoLoadMore() {
		return mIsAutoLoadMore;
	}

	public void setAutoLoadMore(boolean pIsAutoLoadMore) {
		mIsAutoLoadMore = pIsAutoLoadMore;
	}
		
	public boolean isMoveToFirstItemAfterRefresh() {
		return mIsMoveToFirstItemAfterRefresh;
	}

	public void setMoveToFirstItemAfterRefresh(
			boolean pIsMoveToFirstItemAfterRefresh) {
		mIsMoveToFirstItemAfterRefresh = pIsMoveToFirstItemAfterRefresh;
	}
	
	public int getScrollState() {
	    return mScrollState;
	}
	// ============================================================================

    private LayoutInflater mInflater;

	private LinearLayout mHeadView;
	private TextView mTipsTextView;
	//private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private View mProgressBar;
	
	private View mEndRootView;
	private View mEndLoadProgressBar;
	private TextView mEndLoadTipsTextView;

	/**  headView动画   */
	private RotateAnimation mArrowAnim;
	/**  headView反转动画   */
	private RotateAnimation mArrowReverseAnim;
 
	/** 用于保证startY的值在一个完整的touch事件中只被记录一次    */
	private boolean mIsRecored;

	private int mHeadViewWidth;
	private int mHeadViewHeight;

	private int mStartY;
	private boolean mIsBack;
	
	private int mFirstItemIndex;
	private int mLastItemIndex;
	private int mCount;
	private boolean mEnoughCount;//足够数量充满屏幕？ 
	
	private OnRefreshListener mRefreshListener;
	private OnLoadMoreListener mLoadMoreListener;


	public CustomListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public CustomListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public CustomListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}

	/**
	 * 初始化操作
	 * @param pContext 
	 * @version 1.0
	 */
	private void init(Context pContext) {
		mInflater = LayoutInflater.from(pContext);

		addHeadView();
		
		setOnScrollListener(this);

		initPullImageAnimation(0);
		

	}

	/**
	 * 添加下拉刷新的HeadView 
	 * @version 1.0
	 */
	private void addHeadView() {
		mHeadView = (LinearLayout) mInflater.inflate(R.layout.putao_head, null);

		if (mHeadView == null) {
			return;
		}
		mArrowImageView = (ImageView) mHeadView
				.findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		mProgressBar =  mHeadView
				.findViewById(R.id.head_progressBar);
		mTipsTextView = (TextView) mHeadView.findViewById(
				R.id.head_tipsTextView);
//		mLastUpdatedTextView = (TextView) mHeadView
//				.findViewById(R.id.head_lastUpdatedTextView);

		measureView(mHeadView);
		mHeadViewHeight = mHeadView.getMeasuredHeight();
		mHeadViewWidth = mHeadView.getMeasuredWidth();
		
		mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
		mHeadView.invalidate();

		Log.v("size", "width:" + mHeadViewWidth + " height:"
				+ mHeadViewHeight);

		addHeaderView(mHeadView, null, false);
		
		mHeadState = DONE;
	}
	
	/**
	 * 添加加载更多FootView
	 * @version 1.0
	 */
	private void addFooterView() {
		mEndRootView = mInflater.inflate(R.layout.putao_listfooter_more, null);
		mEndRootView.setVisibility(View.GONE);
		mEndLoadProgressBar =  mEndRootView
				.findViewById(R.id.pull_to_refresh_progress);
		mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.load_more);
//		mEndRootView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				if(mCanLoadMore){
//					if(mCanRefresh){
//						// 当可以下拉刷新时，如果FootView没有正在加载，并且HeadView没有正在刷新，才可以点击加载更多。
//						if(mEndState != ENDINT_LOADING && mHeadState != REFRESHING){
//							mEndState = ENDINT_LOADING;
//							onLoadMore();
//						}
//					}else if(mEndState != ENDINT_LOADING){
//						// 当不能下拉刷新时，FootView不正在加载时，才可以点击加载更多。
//						mEndState = ENDINT_LOADING;
//						onLoadMore();
//					}
//				}
//			}
//		});
		
		addFooterView(mEndRootView);
		
		if(mIsAutoLoadMore){
			mEndState = ENDINT_AUTO_LOAD_DONE;
		}else{
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
	}

	/**
	 * 实例化下拉刷新的箭头的动画效果 
	 * @param pAnimDuration 动画运行时长
	 * @version 1.0
	 */
	private void initPullImageAnimation(final int pAnimDuration) {
		
		int _Duration;
		
		if(pAnimDuration > 0){
			_Duration = pAnimDuration;
		}else{
			_Duration = 250;
		}
//		Interpolator _Interpolator;
//		switch (pAnimType) {
//		case 0:
//			_Interpolator = new AccelerateDecelerateInterpolator();
//			break;
//		case 1:
//			_Interpolator = new AccelerateInterpolator();
//			break;
//		case 2:
//			_Interpolator = new AnticipateInterpolator();
//			break;
//		case 3:
//			_Interpolator = new AnticipateOvershootInterpolator();
//			break;
//		case 4:
//			_Interpolator = new BounceInterpolator();
//			break;
//		case 5:
//			_Interpolator = new CycleInterpolator(1f);
//			break;
//		case 6:
//			_Interpolator = new DecelerateInterpolator();
//			break;
//		case 7:
//			_Interpolator = new OvershootInterpolator();
//			break;
//		default:
//			_Interpolator = new LinearInterpolator();
//			break;
//		}
		
		Interpolator _Interpolator = new LinearInterpolator();
		
		mArrowAnim = new RotateAnimation(0, -180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mArrowAnim.setInterpolator(_Interpolator);
		mArrowAnim.setDuration(_Duration);
		mArrowAnim.setFillAfter(true);

		mArrowReverseAnim = new RotateAnimation(-180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mArrowReverseAnim.setInterpolator(_Interpolator);
		mArrowReverseAnim.setDuration(_Duration);
		mArrowReverseAnim.setFillAfter(true);
	}

	/**
	 * 测量HeadView宽高(注意：此方法仅适用于LinearLayout，请读者自己测试验证。)
	 * @param pChild 
	 * @version 1.0
	 */
	private void measureView(View pChild) {
		ViewGroup.LayoutParams p = pChild.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;

		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight,
					MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		pChild.measure(childWidthSpec, childHeightSpec);
	}
	
	/**
	 *为了判断滑动到ListView底部没
	 */
	@Override
	public void onScroll(AbsListView pView, int pFirstVisibleItem,
			int pVisibleItemCount, int pTotalItemCount) {
	    
//	    LogUtil.i("cms", "onScroll pFirst:"+pFirstVisibleItem+" pVisible:"+pVisibleItemCount+" pTotal:"+pTotalItemCount);
	    
		mFirstItemIndex = pFirstVisibleItem;
		mLastItemIndex = pFirstVisibleItem + pVisibleItemCount - 2;
		mCount = pTotalItemCount - 2;
		if (pTotalItemCount > pVisibleItemCount ) {
			mEnoughCount = true;
//			endingView.setVisibility(View.VISIBLE);
		} else {
			mEnoughCount = false;
		}
	}

    @Override
	public void onScrollStateChanged(AbsListView pView, int pScrollState) {
//	    LogUtil.i("cms", "onScrollStateChanged:"+pScrollState);
	    mScrollState = pScrollState;
	    // added by wcy 2015-8-27 start for bug #6557
	    if (pScrollState == SCROLL_STATE_TOUCH_SCROLL)
        {
	        View currentFocus = ((Activity) getContext()).getCurrentFocus();
	        if (currentFocus != null)
            {
                currentFocus.clearFocus();
            }
        }
	    // added by wcy 2015-8-27 end
		if (pScrollState == OnScrollListener.SCROLL_STATE_FLING) {
			if(mAdapter != null && mAdapter.getmImageLoader() != null){
				mAdapter.getmImageLoader().setPauseWork(true);
			}
		} else {
			if(mAdapter != null && mAdapter.getmImageLoader() != null){
				mAdapter.getmImageLoader().setPauseWork(false);
			}
			if(mAdapter != null && pScrollState == SCROLL_STATE_IDLE){
//				mAdapter.recyleScrolledImage(mFirstItemIndex, mLastItemIndex);
			}
		}
		if(mCanLoadMore){// 存在加载更多功能
//		    LogUtil.i("cms", "onScrollStateChanged mCanLoadMore:"+ mLastItemIndex + " mCount:"+mCount);
			if (mLastItemIndex ==  mCount && pScrollState == SCROLL_STATE_IDLE) {
				//SCROLL_STATE_IDLE=0，滑动停止
				if (mEndState != ENDINT_LOADING) {
					if(mIsAutoLoadMore ){// 自动加载更多，我们让FootView显示 “更    多”
					    if(mTouchDownY > mTouchUpY){
						if(mCanRefresh){
							// 存在下拉刷新并且HeadView没有正在刷新时，FootView可以自动加载更多。
							if(mHeadState != REFRESHING){
								// FootView显示 : 更    多  ---> 加载中...
								mEndState = ENDINT_LOADING;
								onLoadMore();
								changeEndViewByState();
							}
						}else{// 没有下拉刷新，我们直接进行加载更多。
							// FootView显示 : 更    多  ---> 加载中...
							mEndState = ENDINT_LOADING;
							onLoadMore();
							changeEndViewByState();
						}
					    }
					}else{// 不是自动加载更多，我们让FootView显示 “点击加载”
						// FootView显示 : 点击加载  ---> 加载中...
						mEndState = ENDINT_MANUAL_LOAD_DONE;
						changeEndViewByState();
					}
				}
			}
		}else if(mEndRootView != null && mEndRootView.getVisibility() == VISIBLE){
			// 突然关闭加载更多功能之后，我们要移除FootView。
//		    LogUtil.i(TAG,"this.removeFooterView(endRootView);...");
			mEndRootView.setVisibility(View.GONE);
			this.removeFooterView(mEndRootView);
		}
	}
	
	/**
	 * 列表中有更多数据，但是第一次加载时未填充ListView
	 * 显示：网络搜索中，请稍候...
	 */
	public void setLoadingAndWaitState(){
		mEndState = ENDINT_LOADING_AND_WAIT;
		changeEndViewByState();
	}
	
	/**
	 * 第一次自动刷新时显示正在刷新
	 */
	public void setRefreshing(){
	    mHeadState = REFRESHING;
        changeHeaderViewByState();
	}
	
	/**
	 * 没有更多数据了
	 */
	public void setHasNoMoreDataState(){
		mEndState = ENDINT_LOAD_NO_MORE;
		changeEndViewByState();
	}

	/**
	 * 改变加载更多状态
	 * @version 1.0
	 */
	private void  changeEndViewByState() {
		if (mCanLoadMore) {
			//允许加载更多
			switch (mEndState) {
			case ENDINT_LOADING://刷新中
				
				//add by ls 2015-03-04 修复bug#3181
				if(getFooterViewsCount()==0){
					addFooterView();
				}
				//add by ls 
				
				mEndLoadTipsTextView.setText(R.string.putao_p2refresh_doing_head_refresh);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.VISIBLE);
				mEndRootView.setVisibility(View.VISIBLE);
				break;
			case ENDINT_MANUAL_LOAD_DONE:// 手动刷新完成
				
				//add by ls 2015-03-04 修复bug#3181
				if(getFooterViewsCount()==0){
					addFooterView();
				}
				//add by ls 
				
				// 点击加载
				mEndLoadTipsTextView.setText(R.string.putao_p2refresh_end_click_load_more);
				mEndLoadTipsTextView.setVisibility(View.GONE);
				mEndLoadProgressBar.setVisibility(View.GONE);
				
				mEndRootView.setVisibility(View.GONE);
				break;
				/* 
                 * add by zj 2015-6-13 start 
                 */
			case ENDINT_LOAD_NO_MORE:// 自动刷新,没有更多数据了
			    // 更    多
			    mEndLoadTipsTextView.setText(R.string.putao_p2refresh_end_load_more);
			    mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			    mEndLoadProgressBar.setVisibility(View.GONE);
			    
			    if(getFooterViewsCount() == 0){
			        addFooterView();
			    }
			    
			    mEndRootView.setVisibility(View.VISIBLE);
			    break;
			    //end 2015-6-13 by zj
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成
				// 更    多
				mEndLoadTipsTextView.setText(R.string.putao_p2refresh_end_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);
 
				
				/*
				 * modify ljq 2015-04-18 start 
				 * 注释掉屏幕是否填充的判定
				 */
				/*
				 * modify by ls 2015-2-9 start BUG#3181
				 * 恢复为原来的代码 modify by ls 2015-03-03 
				 */
//				if(mEnoughCount){
					//add by ls 2015-03-04;
					if(getFooterViewsCount() == 0){
						addFooterView();
					}
					//add by ls
					
					mEndRootView.setVisibility(View.VISIBLE);
//				}else{
//					LogUtil.d("ljq", "ENDINT_AUTO_LOAD_DONE removeFooterView");
//					/*
//					 * modify by ls 2015-03-04; old code:mEndRootView.setVisibility(View.GONE);
//					 * 修复bug# 3181
//					 */
//					removeFooterView(mEndRootView);
//					//modify by ls;
////					mEndRootView.setVisibility(View.GONE);
//				}
//					// end 2015-2-9 by ls  
					// modify ljq 2015-04-18 end 
				break;
			case ENDINT_LOADING_AND_WAIT: //网络搜索中，请稍候...
				mEndLoadTipsTextView.setText(R.string.putao_p2refresh_searching_and_wait);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.VISIBLE);
				mEndRootView.setVisibility(View.VISIBLE);
				break;
			default:
				// 原来的代码是为了： 当所有item的高度小于ListView本身的高度时，
				// 要隐藏掉FootView，大家自己去原作者的代码参考。
				
//				if (enoughCount) {					
//					endRootView.setVisibility(View.VISIBLE);
//				} else {
//					endRootView.setVisibility(View.GONE);
//				}
				break;
			}
		}
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent event) {
		
		if (mCanRefresh) {
			if(mCanLoadMore && mEndState == ENDINT_LOADING){
				// 如果存在加载更多功能，并且当前正在加载更多，默认不允许下拉刷新，必须加载完毕后才能使用。
				return super.onTouchEvent(event);
			}
			
			switch (event.getAction()) {
			
			case MotionEvent.ACTION_DOWN:
				if (mFirstItemIndex == 0 && !mIsRecored) {
					mIsRecored = true;
					mStartY = (int) event.getY();
				}
				break;

			case MotionEvent.ACTION_UP:

				if (mHeadState != REFRESHING && mHeadState != LOADING) {
					if (mHeadState == DONE) {
						
					}
					if (mHeadState == PULL_TO_REFRESH) {
						mHeadState = DONE;
						changeHeaderViewByState();
					}
					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadState = REFRESHING;
						changeHeaderViewByState();
						onRefresh();
					}
				}

				mIsRecored = false;
				mIsBack = false;

				break;

			case MotionEvent.ACTION_MOVE:
				int tempY = (int) event.getY();

				if (!mIsRecored && mFirstItemIndex == 0) {
					mIsRecored = true;
					mStartY = tempY;
				}

				if (mHeadState != REFRESHING && mIsRecored && mHeadState != LOADING) {

					// 保证在设置padding的过程中，当前的位置一直是在head，
					// 否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
					// 可以松手去刷新了
					if (mHeadState == RELEASE_TO_REFRESH) {

						setSelection(0);

						// 往上推了，推到了屏幕足够掩盖head的程度，但是还没有推到全部掩盖的地步
						if (((tempY - mStartY) / RATIO < mHeadViewHeight)
								&& (tempY - mStartY) > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
						// 一下子推到顶了
						else if (tempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeaderViewByState();
						}
						// 往下拉了，或者还没有上推到屏幕顶部掩盖head的地步
					}
					// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
					if (mHeadState == PULL_TO_REFRESH) {

						setSelection(0);

						// 下拉到可以进入RELEASE_TO_REFRESH的状态
						if ((tempY - mStartY) / RATIO >= mHeadViewHeight) {
							mHeadState = RELEASE_TO_REFRESH;
							mIsBack = true;
							changeHeaderViewByState();
						} else if (tempY - mStartY <= 0) {
							mHeadState = DONE;
							changeHeaderViewByState();
						}
					}

					if (mHeadState == DONE) {
						if (tempY - mStartY > 0) {
							mHeadState = PULL_TO_REFRESH;
							changeHeaderViewByState();
						}
					}

					if (mHeadState == PULL_TO_REFRESH) {
						mHeadView.setPadding(0, -1 * mHeadViewHeight
								+ (tempY - mStartY) / RATIO, 0, 0);

					}

					if (mHeadState == RELEASE_TO_REFRESH) {
						mHeadView.setPadding(0, (tempY - mStartY) / RATIO
								- mHeadViewHeight, 0, 0);
					}
				}
				break;
			}
		}
        switch (event.getAction())
        {

            case MotionEvent.ACTION_DOWN:
                mTouchDownY = (int) event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                if (!upFlag)
                {
                    mTouchDownY = (int) event.getY();
                    upFlag = true;
                }
                break;
            case MotionEvent.ACTION_UP:

                mTouchUpY = (int) event.getY();
                upFlag =false;
                break;
        }
		return super.onTouchEvent(event);
	}

	/**
	 * 当HeadView状态改变时候，调用该方法，以更新界面
	 * @version 1.0
	 */
	private void changeHeaderViewByState() {
		switch (mHeadState) {
		case RELEASE_TO_REFRESH:
			mArrowImageView.setVisibility(View.VISIBLE);
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			//mLastUpdatedTextView.setVisibility(View.VISIBLE);

//			mArrowImageView.clearAnimation();
//			mArrowImageView.startAnimation(mArrowAnim);
			// 松开刷新
			/*
             * modify by putao_lhq at 2015年1月9日 @start
             * old code:
			mTipsTextView.setText(R.string.putao_p2refresh_release_refresh);
			 */
			if (TextUtils.isEmpty(mTipString)) {
			    mTipsTextView.setText(R.string.putao_p2refresh_release_refresh);
			} else {
			    mTipsTextView.setText(mTipString);
			}/* end by putao_lhq */
			
			break;
		case PULL_TO_REFRESH:
			mProgressBar.setVisibility(View.GONE);
			mTipsTextView.setVisibility(View.VISIBLE);
			//mLastUpdatedTextView.setVisibility(View.VISIBLE);
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setVisibility(View.VISIBLE);
			// 是由RELEASE_To_REFRESH状态转变来的
			if (mIsBack) {
				mIsBack = false;
//				mArrowImageView.clearAnimation();
//				mArrowImageView.startAnimation(mArrowReverseAnim);
				// 下拉刷新
				/*
                 * modify by putao_lhq at 2015年1月9日 @start
                 * old code:
				mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
				 */
				if (TextUtils.isEmpty(mTipString)) {
				    mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
				} else {
				    mTipsTextView.setText(mTipString);
				}/* end by putao_lhq */
				
			} else {
				// 下拉刷新
			    /*
                 * modify by putao_lhq at 2015年1月9日 @start
                 * old code:
				mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
			     */
			    if (TextUtils.isEmpty(mTipString)) {
			        mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
			    } else {
			        mTipsTextView.setText(mTipString);
			    }/* end by putao_lhq */
			    
			}
			break;

		case REFRESHING:
//			mHeadView.setPadding(0, 0, 0, 0);
			smoothTo(mHeadView.getPaddingTop(), 0);
			
			// 华生的建议： 实际上这个的setPadding可以用动画来代替。我没有试，但是我见过。其实有的人也用Scroller可以实现这个效果，
			// 我没时间研究了，后期再扩展，这个工作交给小伙伴你们啦~ 如果改进了记得发到我邮箱噢~
			// 本人邮箱： xxzhaofeng5412@gmail.com
			
			mProgressBar.setVisibility(View.VISIBLE);
//			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);
			// 正在刷新...
			/*
             * modify by putao_lhq at 2015年1月9日 @start
             * old code:
			mTipsTextView.setText(R.string.putao_p2refresh_doing_head_refresh);
			 */
			if (TextUtils.isEmpty(mTipDoingString)) {
			    mTipsTextView.setText(R.string.putao_p2refresh_doing_head_refresh);
			} else {
			    mTipsTextView.setText(mTipDoingString);
			}/* end by putao_lhq */
			
			//mLastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		case DONE:
//			mHeadView.setPadding(0, -1 * mHeadViewHeight, 0, 0);
			smoothTo(mHeadView.getPaddingTop(), -1 * mHeadViewHeight);
			mArrowImageView.setVisibility(View.VISIBLE);
			// 此处可以改进，同上所述。
			
			mProgressBar.setVisibility(View.GONE);
//			mArrowImageView.clearAnimation();
//			mArrowImageView.setImageResource(R.drawable.putao_arrow);
			// 下拉刷新
			/*
             * modify by putao_lhq at 2015年1月9日 @start
             * old code:
			mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
			 */
			if (TextUtils.isEmpty(mTipString)) {
			    mTipsTextView.setText(R.string.putao_p2refresh_pull_to_refresh);
			} else {
			    mTipsTextView.setText(mTipString);
			}/* end by putao_lhq */
			
			//mLastUpdatedTextView.setVisibility(View.VISIBLE);

			break;
		}
	}

	/**
	 * 下拉刷新监听接口
	 * @version 1.0
	 */
	public interface OnRefreshListener {
		public void onRefresh();
	}
	
	/**
	 * 加载更多监听接口
	 * @version 1.0
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	public void setOnRefreshListener(OnRefreshListener pRefreshListener) {
		if(pRefreshListener != null){
			mRefreshListener = pRefreshListener;
			mCanRefresh = true;
		}
	}

	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if(pLoadMoreListener != null){
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
			if(mCanLoadMore && getFooterViewsCount() == 0){
				addFooterView();
			}
		}
	}
	
	/**
	 * 正在下拉刷新
	 * @version 1.0
	 */
	private void onRefresh() {
		if (mRefreshListener != null) {
			mRefreshListener.onRefresh();
		}
	}
	
	/**
	 * 下拉刷新完成
	 * @version 1.0
	 */
	public void onRefreshComplete() {
		// 下拉刷新后是否显示第一条Item 
		if(mIsMoveToFirstItemAfterRefresh){
			setSelection(0);
		}
		
		mHeadState = DONE;
		// 最近更新: Time
//		mLastUpdatedTextView.setText(
//				getResources().getString(R.string.putao_p2refresh_refresh_lasttime) + 
//				new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		changeHeaderViewByState();
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 * @version 1.0
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			// 加载中...
			mEndLoadTipsTextView.setText(R.string.putao_p2refresh_doing_end_refresh);
			mEndLoadTipsTextView.setVisibility(View.VISIBLE);
			mEndLoadProgressBar.setVisibility(View.VISIBLE);
			mLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成 
	 * @version 1.0
	 */
	public void onLoadMoreComplete(boolean hasComplete) {
		if(mIsAutoLoadMore){
			mEndState = ENDINT_AUTO_LOAD_DONE;
		}else{
			mEndState = ENDINT_MANUAL_LOAD_DONE;
		}
		if(hasComplete){
			changeEndViewByState();
		}
	}
	
	public View getHeadView() 
	{
	    return mHeadView;
	}
	
	/**
	 * 手动结束/关闭底部加载框
	 */
	public void onManualComplete(){
		mEndState = ENDINT_MANUAL_LOAD_DONE;
		changeEndViewByState();
	}
	
	/**
	 * 主要更新一下刷新时间啦！
	 * @param adapter
	 * @version 1.0
	 */
	public void setAdapter(BaseAdapter adapter) {
		// 最近更新: Time
//		mLastUpdatedTextView.setText(
//				getResources().getString(R.string.putao_p2refresh_refresh_lasttime) + 
//				new SimpleDateFormat(DATE_FORMAT_STR, Locale.CHINA).format(new Date()));
		super.setAdapter(adapter);
		mAdapter = (BaseListViewAdapter) adapter;
	}

	public void setFooterViewVisibility(int visibility){
		if(mEndRootView != null){
			mEndRootView.setVisibility(visibility);
		}
	}
	
	public void hideListViewFooterDivider() {
	    if (mEndRootView != null)
        {
//            mEndRootView.findViewById(R.id.custom_list_view_foot_divider).setVisibility(View.GONE);
        }
	}
	
	 SmoothScrollRunnable mSmoothScrollRunnable;

	    protected void smoothTo(int currenMargin, int targetMargin) {
	        if (this.mSmoothScrollRunnable != null) {
	            this.mSmoothScrollRunnable.stop();
	        }

	        this.mSmoothScrollRunnable = new SmoothScrollRunnable(currenMargin, targetMargin);
	        this.mHandler.post(this.mSmoothScrollRunnable);
	    }

	    final class SmoothScrollRunnable implements Runnable {
	        private final Interpolator interpolator;

	        private final int to;

	        private final int from;

	        private boolean continueRunning = true;

	        private long startTime = -1L;

	        private int current = -1;

	        public SmoothScrollRunnable(int from, int to) {
	            this.from = from;
	            this.to = to;
	            this.interpolator = new AccelerateDecelerateInterpolator();
	        }

	        @Override
	        public void run() {
	            if (this.startTime == -1L) {
	                this.startTime = System.currentTimeMillis();
	            } else {
	                long l = 1000L * (System.currentTimeMillis() - this.startTime) / 190L;
	                l = Math.max(Math.min(l, 1000L), 0L);

	                int i = Math.round((this.from - this.to)
	                        * this.interpolator.getInterpolation(l / 1000.0F));

	                this.current = (this.from - i);
//	                RelativeLayout.LayoutParams params2 = (android.widget.RelativeLayout.LayoutParams)getLayoutParams();
//	                params2.setMargins(0, current, 0, 0);
//	                setLayoutParams(params2);
	                
	                mHeadView.setPadding(0, current, 0, 0);
	                
	            }

	            if ((this.continueRunning) && (this.to != this.current))
	                mHandler.postDelayed(this, 10L);
	        }

	        public void stop() {
	            this.continueRunning = false;
	            mHandler.removeCallbacks(this);
	        }
	    }

	    private final Handler mHandler = new Handler();
	    
}
